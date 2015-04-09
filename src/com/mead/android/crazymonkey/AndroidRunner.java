package com.mead.android.crazymonkey;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PushbackInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.mead.android.crazymonkey.build.Builder;
import com.mead.android.crazymonkey.model.HardwareProperty;
import com.mead.android.crazymonkey.model.Task;
import com.mead.android.crazymonkey.model.Task.STATUS;
import com.mead.android.crazymonkey.process.ArgumentListBuilder;
import com.mead.android.crazymonkey.process.Callable;
import com.mead.android.crazymonkey.process.ForkOutputStream;
import com.mead.android.crazymonkey.process.LocalProc;
import com.mead.android.crazymonkey.process.StreamCopyThread;
import com.mead.android.crazymonkey.sdk.AndroidSdk;
import com.mead.android.crazymonkey.sdk.SdkInstallationException;
import com.mead.android.crazymonkey.sdk.Tool;
import com.mead.android.crazymonkey.util.Utils;

public class AndroidRunner extends AbstractRunner {

	
	public AndroidRunner(CrazyMonkeyBuild build, Task task, AndroidSdk androidSdk, StreamTaskListener taskListener) {
		super(build, task, androidSdk, taskListener);
	}
	
	public boolean tearDown() throws IOException, InterruptedException {
		cleanUp(build, task.getEmulator(), context);
		return true;
	}

	public boolean createAvd() throws InterruptedException, EmulatorCreationException {
		
        if (logger == null) {
            logger = taskListener.getLogger();
        }
        
        AndroidEmulator emulator = task.getEmulator();
        String avdName = emulator.getAvdName();
        String sdCardSize = emulator.getSdCardSize();
        AndroidPlatform osVersion = emulator.getOsVersion();
        ScreenResolution screenResolution = emulator.getScreenResolution();
        
        String targetAbi = emulator.getTargetAbi();

        final File homeDir = Utils.getHomeDirectory(androidSdk.getSdkHome());
        final File avdDirectory = Utils.getAvdDirectory(homeDir, avdName);
        final boolean emulatorExists = Utils.getAvdConfigFile(homeDir, avdName).exists();

        // Check whether AVD needs to be created
        boolean createSdCard = false;
        boolean createSnapshot = false;
		File snapshotsFile = new File(Utils.getAvdDirectory(homeDir, avdName), "snapshots.img");
        if (emulatorExists) {
            // AVD exists: check whether there's anything still to be set up
            File sdCardFile = new File(Utils.getAvdDirectory(homeDir, avdName), "sdcard.img");
            boolean sdCardRequired = sdCardSize != null;

            // Check if anything needs to be done for snapshot-enabled builds
            if (emulator.isUseSnapshots() && androidSdk.supportsSnapshots()) {
                if (!snapshotsFile.exists()) {
                    createSnapshot = true;
                }

                // We should ensure that we start out with a clean SD card for the build
                if (sdCardRequired && sdCardFile.exists()) {
                    sdCardFile.delete();
                }
            }

            // Flag that we need to generate an SD card, if there isn't one existing
            if (sdCardRequired && !sdCardFile.exists()) {
                createSdCard = true;
            }

            // If everything is ready, then return
            if (!createSdCard && !createSnapshot) {
                return true;
            }
        } else {
            AndroidEmulator.log(logger, Messages.CREATING_AVD(avdDirectory));
        }

        // We can't continue if we don't know where to find emulator images or tools
        if (!androidSdk.hasKnownRoot()) {
            throw new EmulatorCreationException(Messages.SDK_NOT_SPECIFIED());
        }
        final File sdkRoot = new File(androidSdk.getSdkRoot());
        if (!sdkRoot.exists()) {
            throw new EmulatorCreationException(Messages.SDK_NOT_FOUND(androidSdk.getSdkRoot()));
        }

        // If we need to initialise snapshot support for an existing emulator, do so
        if (createSnapshot) {
            // Copy the snapshots file into place
            File snapshotDir = new File(sdkRoot, "tools/lib/emulator");
            Utils.copyFile(new File(snapshotDir, "snapshots.img"), snapshotsFile);

            // Update the AVD config file mark snapshots as enabled
            Map<String, String> configValues;
            try {
                configValues = Utils.parseAvdConfigFile(homeDir, avdName);
                configValues.put("snapshot.present", "true");
                Utils.writeAvdConfigFile(homeDir, configValues, avdName);
            } catch (IOException e) {
                throw new EmulatorCreationException(Messages.AVD_CONFIG_NOT_READABLE(), e);
            }
        }

        // If we need create an SD card for an existing emulator, do so
        if (createSdCard) {
            AndroidEmulator.log(logger, Messages.ADDING_SD_CARD(sdCardSize, avdName));
            if (!createSdCard(homeDir)) {
                throw new EmulatorCreationException(Messages.SD_CARD_CREATION_FAILED());
            }

            // Update the AVD config file
            Map<String, String> configValues;
            try {
                configValues = Utils.parseAvdConfigFile(homeDir, avdName);
                configValues.put("sdcard.size", sdCardSize);
                Utils.writeAvdConfigFile(homeDir, configValues, avdName);
            } catch (IOException e) {
                throw new EmulatorCreationException(Messages.AVD_CONFIG_NOT_READABLE(), e);
            }
        }

        // Return if everything is now ready for use
        if (emulatorExists) {
            return true;
        }

        // Build up basic arguments to `android` command
        final StringBuilder args = new StringBuilder(100);
        args.append("create avd ");

        // Overwrite any existing files
        args.append("-f ");

        // Initialise snapshot support, regardless of whether we will actually use it
        if (androidSdk.supportsSnapshots()) {
            args.append("-a ");
        }

        if (emulator.getSdCardSize() != null) {
            args.append("-c ");
            args.append(emulator.getSdCardSize());
            args.append(" ");
        }
        args.append("-s ");
        args.append(screenResolution.getSkinName());
        args.append(" -n ");
        args.append(emulator.getAvdName());
        boolean isUnix = !Utils.isWindows();
        ArgumentListBuilder builder = Utils.getToolCommand(androidSdk, isUnix, Tool.ANDROID, args.toString());

        // Tack on quoted platform name at the end, since it can be anything
        builder.add("-t");
        builder.add(osVersion.getTargetName());

        if (emulator.getTargetAbi() != null && osVersion.requiresAbi()) {
            // This is an unpleasant side-effect of there being an ABI for android-10,
            // and that Google renamed the image after its initial release from Intel...
            // Ideally, as stated in AndroidPlatform#requiresAbi, we should preferably check
            // via the "android list target" command whether an ABI is actually required.
            if (osVersion.getSdkLevel() != 10 || targetAbi.equals("armeabi")
                    || targetAbi.equals("x86")) {
                builder.add("--abi");
                builder.add(targetAbi);
            }
        }

        // Log command line used, for info
        AndroidEmulator.log(logger, builder.toStringWithQuote());

        // Run!
        boolean avdCreated = false;
        final Process process;
        try {
            ProcessBuilder procBuilder = new ProcessBuilder(builder.toList());
            if (androidSdk.hasKnownHome()) {
                procBuilder.environment().put("ANDROID_SDK_HOME", androidSdk.getSdkHome());
            }
            process = procBuilder.start();
        } catch (IOException ex) {
            throw new EmulatorCreationException(Messages.AVD_CREATION_FAILED());
        }

        // Redirect process's stderr to a stream, for logging purposes
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        new StreamCopyThread("", process.getErrorStream(), stderr).start();

        // Command may prompt us whether we want to further customise the AVD.
        // Just "press" Enter to continue with the selected target's defaults.
        try {
            boolean processAlive = true;

            // Block until the command outputs something (or process ends)
            final PushbackInputStream in = new PushbackInputStream(process.getInputStream(), 10);
            int len = in.read();
            if (len == -1) {
                // Check whether the process has exited badly, as sometimes no output is valid.
                // e.g. When creating an AVD with Google APIs, no user input is requested.
                if (process.waitFor() != 0) {
                    AndroidEmulator.log(logger, Messages.AVD_CREATION_FAILED());
                    AndroidEmulator.log(logger, stderr.toString(), true);
                    throw new EmulatorCreationException(Messages.AVD_CREATION_FAILED());
                }
                processAlive = false;
            }
            in.unread(len);

            // Write CRLF, if required
            if (processAlive) {
                final OutputStream stream = process.getOutputStream();
                stream.write('\r');
                stream.write('\n');
                stream.flush();
                stream.close();
            }

            // read the rest of stdout (for debugging purposes)
            Utils.copyStream(in, stdout);
            in.close();

            // Wait for happy ending
            if (process.waitFor() == 0) {
                // Do a sanity check to ensure the AVD was really created
                avdCreated = Utils.getAvdConfigFile(homeDir, avdName).exists();
            }

        } catch (IOException e) {
            throw new EmulatorCreationException(Messages.AVD_CREATION_ABORTED(), e);
        } catch (InterruptedException e) {
            throw new EmulatorCreationException(Messages.AVD_CREATION_INTERRUPTED(), e);
        } finally {
            process.destroy();
        }

        // For reasons unknown, the return code may not be correctly reported on Windows.
        // So check whether stderr contains failure info (useful for other platforms too).
        String errOutput = stderr.toString();
        String output = stdout.toString();
        if (errOutput.contains("list targets")) {
            AndroidEmulator.log(logger, Messages.INVALID_AVD_TARGET(osVersion.getTargetName()));
            avdCreated = false;
            errOutput = null;
        } else if (errOutput.contains("more than one ABI")) {
            AndroidEmulator.log(logger, Messages.MORE_THAN_ONE_ABI(osVersion.getTargetName(), output), true);
            avdCreated = false;
            errOutput = null;
        }

        // Check everything went ok
        if (!avdCreated) {
            if (errOutput != null && errOutput.length() != 0) {
                AndroidEmulator.log(logger, stderr.toString(), true);
            }
            throw new EmulatorCreationException(Messages.AVD_CREATION_FAILED());
        }

        // Done!
        return false;
    }
	
	public boolean createSdCard(File homeDir) {

		AndroidEmulator emu = task.getEmulator();

		// Build command: mksdcard 32M /home/foo/.android/avd/whatever.avd/sdcard.img
		ArgumentListBuilder builder = Utils.getToolCommand(androidSdk, !Utils.isWindows(), Tool.MKSDCARD, null);
		builder.add(emu.getSdCardSize());
		builder.add(new File(Utils.getAvdDirectory(homeDir, emu.getAvdName()), "sdcard.img"));

		// Run!
		try {
			ProcessBuilder procBuilder = new ProcessBuilder(builder.toList());
			if (androidSdk.getSdkHome() != null) {
				procBuilder.environment().put("ANDROID_SDK_HOME", androidSdk.getSdkHome());
			}
			procBuilder.start().waitFor();
		} catch (InterruptedException ex) {
			return false;
		} catch (IOException ex) {
			return false;
		}

		return true;
	}
	
	public void configAvd() throws IOException {
		
		if (logger == null) {
			logger = taskListener.getLogger();
        }
		
		AndroidEmulator emulator = task.getEmulator();
		String avdName = emulator.getAvdName();
		HardwareProperty[] hardwareProperties = emulator.getHardwareProperties();

        final File homeDir = Utils.getHomeDirectory(androidSdk.getSdkHome());

        // Parse the AVD's config
        Map<String, String> configValues;
        configValues = Utils.parseAvdConfigFile(homeDir, avdName);

        // Insert any hardware properties we want to override
        AndroidEmulator.log(logger, Messages.SETTING_HARDWARE_PROPERTIES());
        for (HardwareProperty prop : hardwareProperties) {
            AndroidEmulator.log(logger, String.format("%s: %s", prop.getKey(), prop.getValue()), true);
            configValues.put(prop.getKey(), prop.getValue());
        }

        // Update config file
        Utils.writeAvdConfigFile(homeDir, configValues, avdName);
        
		return;
	}
	
	public synchronized void restoreBackup() throws IOException, InterruptedException {
		
		if (logger == null) {
			logger = taskListener.getLogger();
        }
		String script = build.getTestScriptPath() + "//restore_backup.bat";
		
		if (Utils.isUnix()) {
			script = build.getTestScriptPath() + "//restore_backup.sh";
		}
		
		List<String> args = new ArrayList<String>();
		args.add(context.getSerial());
		args.add(build.getUserDataPath() + "//" + task.getAppRunner().getAppId() + "//backup.ab");
		
		String androidToolsDir = "";
		if (androidSdk.hasKnownRoot()) {
			try {
				androidToolsDir = androidSdk.getSdkRoot() + Tool.EMULATOR.findInSdk(androidSdk);
			} catch (SdkInstallationException e) {
				androidToolsDir = "";
			}
		} else {
			androidToolsDir = "";
		}
		
		args.add(androidToolsDir);
		Builder builder = this.getBuilder(script, args);
		boolean result = builder.perform(build, androidSdk, task.getEmulator(), context, taskListener, "Success");
		if (!result) {
			log(logger, String.format("Restore the apk bakcup '%s' failed.", script));
			task.setStatus(STATUS.NOT_BUILT);
		} else {
			log(logger, String.format("Restore the apk bakcup '%s' scussfully.", script));
		}
		return;
	}

	public boolean startUp () throws InterruptedException, IOException {
		
        final PrintStream logger = taskListener.getLogger();
        
        AndroidEmulator emuConfig = task.getEmulator();
        boolean useSnapshots = emuConfig.isUseSnapshots();
        //boolean wipeData = emuConfig.isWipeData();
        
        HardwareProperty[] hardwareProperties = emuConfig.getHardwareProperties();

        // First ensure that emulator exists
        final boolean emulatorAlreadyExists;
        
        try {
            emulatorAlreadyExists = this.createAvd();
        } catch (AndroidEmulatorException ex) {
            log(logger, Messages.COULD_NOT_CREATE_EMULATOR(ex.getMessage()));
            task.setStatus(STATUS.NOT_BUILT);
            return false;
        }

        if (hardwareProperties != null && hardwareProperties.length != 0) {
            this.configAvd();
        }
        
        // Delay start up by the configured amount of time
        final int delaySecs = build.getStartUpDelay();
        if (delaySecs > 0) {
            log(logger, Messages.DELAYING_START_UP(delaySecs));
            Thread.sleep(delaySecs * 1000);
        }
        
        int[] ports = new int[2];
        ports[0] = 0;
        ports[1] = 0;
        
        int maxTryNum = 10;
        int tryNum = 0;
		do {
			tryNum++;
			ports = build.getNextPorts();
		} while ((ports[0] == 0 || ports[1] == 0) && tryNum < maxTryNum);
		
		if (tryNum == maxTryNum) {
			log(logger, "There is no available ports for the emulator.");
			return false;
		}
        
        final AndroidEmulatorContext emu = new AndroidEmulatorContext(build, ports, androidSdk, taskListener);
        this.setContext(emu);
        
        //this.configPhoneInfo();

        // We manually start the adb-server so that later commands will not have to start it,
        // allowing them to complete faster.
        LocalProc adbStart = emu.getToolProcStarter(Tool.ADB, "start-server").stdout(logger).start();
        adbStart.joinWithTimeout(5L, TimeUnit.SECONDS, taskListener);
        LocalProc adbStart2 = emu.getToolProcStarter(Tool.ADB, "start-server").stdout(logger).start();
        adbStart2.joinWithTimeout(5L, TimeUnit.SECONDS, taskListener);

        // Determine whether we need to create the first snapshot
        final SnapshotState snapshotState;
        if (useSnapshots && androidSdk.supportsSnapshots()) {
            boolean hasSnapshot = emuConfig.hasExistingSnapshot(androidSdk, taskListener);
            if (hasSnapshot) {
                // Boot from the existing "jenkins" snapshot
                snapshotState = SnapshotState.BOOT;
            } else {
                // Create an initial "jenkins" snapshot...
                snapshotState = SnapshotState.INITIALISE;
                // ..with a clean start
                emuConfig.setShouldWipeData();
            }
        } else {
            // If snapshots are disabled or not supported, there's nothing to do
            snapshotState = SnapshotState.NONE;
        }
        
        /*
         * TODO add some control for the new and alive scripts
        if (task.getAppRunner() != null && task.getAppRunner().getScriptType().equals("New")) {
        	emuConfig.setWipeData(true);
        } else if (task.getAppRunner() != null && task.getAppRunner().getScriptType().equals("Alive")) {
        	emuConfig.setCommandLineOptions(emuConfig.getCommandLineOptions() + " -data userdata-qemu-old.img -initdata userdata-old.img");
        }
        */
        
        // Compile complete command for starting emulator
        final String emulatorArgs = emuConfig.getCommandArguments(snapshotState,
                androidSdk.supportsSnapshots(), emu.getUserPort(), emu.getAdbPort());
        
        log(logger, "[Android] run emulator with agrumenets: " + emulatorArgs);

        // Start emulator process
        if (snapshotState == SnapshotState.BOOT) {
            log(logger, Messages.STARTING_EMULATOR_FROM_SNAPSHOT());
        } else if (snapshotState == SnapshotState.INITIALISE) {
            log(logger, Messages.STARTING_EMULATOR_SNAPSHOT_INIT());
        } else {
            log(logger, Messages.STARTING_EMULATOR());
        }
        if (emulatorAlreadyExists && emuConfig.isWipeData()) {
            log(logger, Messages.ERASING_EXISTING_EMULATOR_DATA());
        }
        final long bootTime = System.currentTimeMillis();

        // Prepare to capture and log emulator standard output
        ByteArrayOutputStream emulatorOutput = new ByteArrayOutputStream();
        ForkOutputStream emulatorLogger = new ForkOutputStream(logger, emulatorOutput);
        final File homeDir = Utils.getHomeDirectory(emu.getSdk().getSdkHome());
        		
		final LocalProc emulatorProcess = emu.getToolProcStarter(emuConfig.getExecutable(), emulatorArgs)
				.stdout(emulatorLogger).pwd(Utils.getAvdDirectory(homeDir, emuConfig.getAvdName())).start();
        emu.setEmulatorProcess(emulatorProcess);

        // Give the emulator process a chance to initialise
        Thread.sleep(5 * 1000);

        // Check whether a failure was reported on stdout
        if (emulatorOutput.toString().contains("image is used by another emulator")) {
            log(logger, Messages.EMULATOR_ALREADY_IN_USE(emuConfig.getAvdName()));
            return false;
        }

        // Wait for TCP socket to become available
        boolean socket = waitForSocket(build, emu.getAdbPort(), CrazyMonkeyBuild.ADB_CONNECT_TIMEOUT_MS);
        if (!socket) {
            log(logger, Messages.EMULATOR_DID_NOT_START());
            task.setStatus(STATUS.NOT_BUILT);
            //cleanUp(build, emuConfig, emu);
            return false;
        }

        // As of SDK Tools r12, "emulator" is no longer the main process; it just starts a certain
        // child process depending on the AVD architecture.  Therefore on Windows, checking the
        // status of this original process will not work, as it ends after it has started the child.
        //
        // With the adb socket open we know the correct process is running, so we set this flag to
        // indicate that any methods wanting to check the "emulator" process state should ignore it.
        boolean ignoreProcess = !Utils.isUnix() && androidSdk.getSdkToolsVersion() >= 12;

        // Notify adb of our existence
        int result = emu.getToolProcStarter(Tool.ADB, "connect " + emu.getSerial()).join();
        if (result != 0) { // adb currently only ever returns 0!
            log(logger, Messages.CANNOT_CONNECT_TO_EMULATOR());
            //build.setResult(Result.NOT_BUILT);
            task.setStatus(STATUS.NOT_BUILT);
            //cleanUp(build, emuConfig, emu);
            return false;
        }

        // Monitor device for boot completion signal
        log(logger, Messages.WAITING_FOR_BOOT_COMPLETION());
        int bootTimeout = CrazyMonkeyBuild.BOOT_COMPLETE_TIMEOUT_MS;
        if (!emulatorAlreadyExists || emuConfig.isWipeData() || snapshotState == SnapshotState.INITIALISE) {
            bootTimeout *= 2;
        }
        
        boolean bootSucceeded = waitForBootCompletion(ignoreProcess, bootTimeout, emuConfig, emu);
        if (!bootSucceeded) {
            if ((System.currentTimeMillis() - bootTime) < bootTimeout) {
                log(logger, Messages.EMULATOR_STOPPED_DURING_BOOT());
            } else {
                log(logger, Messages.BOOT_COMPLETION_TIMED_OUT(bootTimeout / 1000));
            }
            //build.setResult(Result.NOT_BUILT);
            task.setStatus(STATUS.NOT_BUILT);
            //cleanUp(build, emuConfig, emu);
            return false;
        }

        
        // Unlock emulator by pressing the Menu key once, if required.
        // Upon first boot (and when the data is wiped) the emulator is already unlocked
        final long bootDuration = System.currentTimeMillis() - bootTime;
        //if (emulatorAlreadyExists && !wipeData && snapshotState != SnapshotState.BOOT) {
        // Even if the emulator has started, we generally need to wait longer before the lock
        // screen is up and ready to accept key presses.
        // The delay here is a function of boot time, i.e. relative to the slowness of the host
        Thread.sleep(bootDuration / 2);

        log(logger, Messages.UNLOCKING_SCREEN());
        final String keyEventArgs = String.format("-s %s shell input keyevent %%d", emu.getSerial());
        final String menuArgs = String.format(keyEventArgs, 82);
        ArgumentListBuilder menuCmd = emu.getToolCommand(Tool.ADB, menuArgs);
        emu.getProcStarter(menuCmd).join();

        // If a named emulator already existed, it may not have been booted yet, so the screen
        // wouldn't be locked.  Similarly, an non-named emulator may have already booted the
        // first time without us knowing.  In both cases, we press Back after Menu to compensate
        final String backArgs = String.format(keyEventArgs, 4);
        ArgumentListBuilder backCmd = emu.getToolCommand(Tool.ADB, backArgs);
        emu.getProcStarter(backCmd).join();
        //}

        // Initialise snapshot image, if required
        if (snapshotState == SnapshotState.INITIALISE) {
            // In order to create a clean initial snapshot, give the system some more time to settle
            log(logger, Messages.WAITING_INITIAL_SNAPSHOT());
            Thread.sleep((long) (bootDuration * 0.8));

            // Clear main log before creating snapshot
            final String clearArgs = String.format("-s %s logcat -c", emu.getSerial());
            ArgumentListBuilder adbCmd = emu.getToolCommand(Tool.ADB, clearArgs);
            emu.getProcStarter(adbCmd).join();
            final String msg = Messages.LOG_CREATING_SNAPSHOT();
            final String logArgs = String.format("-s %s shell log -p v -t Jenkins '%s'", emu.getSerial(), msg);
            adbCmd = emu.getToolCommand(Tool.ADB, logArgs);
            emu.getProcStarter(adbCmd).join();

            // Pause execution of the emulator
            boolean stopped = emu.sendCommand("avd stop");
            if (stopped) {
                // Attempt snapshot generation
                log(logger, Messages.EMULATOR_PAUSED_SNAPSHOT());
                int creationTimeout = AndroidEmulatorContext.EMULATOR_COMMAND_TIMEOUT_MS * 4;
                boolean success = emu.sendCommand("avd snapshot save "+ Constants.SNAPSHOT_NAME, creationTimeout);
                if (!success) {
                    log(logger, Messages.SNAPSHOT_CREATION_FAILED());
                }

                // Restart emulator execution
                boolean restarted = emu.sendCommand("avd start");
                if (!restarted) {
                    log(logger, Messages.EMULATOR_RESUME_FAILED());
                    //cleanUp(build, emuConfig, emu);
                    return false;
                }
            } else {
                log(logger, Messages.SNAPSHOT_CREATION_FAILED());
            }
        }

        // Done!
        final long bootCompleteTime = System.currentTimeMillis();
        log(logger, Messages.EMULATOR_IS_READY((bootCompleteTime - bootTime) / 1000));

		return true;
	}
	
    /**
     * Waits for a socket on the remote machine's localhost to become available, or times out.
     *
     * @param launcher The launcher for the remote node.
     * @param port The port to try and connect to.
     * @param timeout How long to keep trying (in milliseconds) before giving up.
     * @return <code>true</code> if the socket was available, <code>false</code> if we timed-out.
     */
    private boolean waitForSocket(CrazyMonkeyBuild launcher, int port, int timeout) {
        try {
            LocalPortOpenTask task = new LocalPortOpenTask(port, timeout);
            return launcher.getChannel().call(task);
        } catch (InterruptedException ex) {
            // Ignore
        }

        return false;
    }
    
    
    /**
     * Called when this wrapper needs to exit, so we need to clean up some processes etc.
     * @param emulatorConfig The emulator being run.
     * @param emu The emulator context
     * @param logcatProcess The adb logcat process.
     * @param logcatFile The file the logcat output is being written to.
     * @param logcatStream The stream the logcat output is being written to.
     * @param artifactsDir The directory where build artifacts should go.
     */
    private void cleanUp(CrazyMonkeyBuild build, AndroidEmulator emulatorConfig, AndroidEmulatorContext emu) throws IOException, InterruptedException {
        // FIXME: Sometimes on Windows neither the emulator.exe nor the adb.exe processes die.
        //        Launcher.kill(EnvVars) does not appear to help either.
        //        This is (a) inconsistent; (b) very annoying.
    	try {
	        // Stop emulator process
	        log(emu.logger(), Messages.STOPPING_EMULATOR());
	        
	        boolean killed = emu.sendCommand("kill");
	
	        // Ensure the process is dead
	        if (!killed && emu.getProcess().isAlive()) {
	            // Give up trying to kill it after a few seconds, in case it's deadlocked
	            killed = Utils.killProcess(emu.getProcess(), CrazyMonkeyBuild.KILL_PROCESS_TIMEOUT_MS);
	            if (!killed) {
            		log(emu.logger(), Messages.EMULATOR_SHUTDOWN_FAILED());
            	}
	        }
	        /*
	        ArgumentListBuilder adbKillCmd = emu.getToolCommand(Tool.ADB, "kill-server");
	        emu.getProcStarter(adbKillCmd).join();
			*/
	        // Delete the emulator, if required
	        if (emulatorConfig.isDeleteAfterBuild()) {
	            try {
	                Callable<Boolean, Exception> deletionTask = new EmulatorDeletionTask(emu, emulatorConfig);
	                emu.getBuild().getChannel().call(deletionTask);
	            } catch (Exception ex) {
	                log(emu.logger(), Messages.FAILED_TO_DELETE_AVD(ex.getLocalizedMessage()));
	            } catch (Throwable e) {
	            	log(emu.logger(), Messages.FAILED_TO_DELETE_AVD(e.getLocalizedMessage()));
					e.printStackTrace();
				}
	        }
        } finally {
        	build.freePorts(new int[]{emu.getAdbPort(), emu.getUserPort()});
            build.freeEmulator(emulatorConfig.getAvdName());
        }
    }
    
    /** A task that deletes the AVD corresponding to our local state. */
    public final class EmulatorDeletionTask implements Callable<Boolean, Exception> {

        private static final long serialVersionUID = 1L;

        private final StreamTaskListener listener;
        private transient PrintStream logger;
        private AndroidEmulatorContext emuContext;
        private AndroidEmulator emu;

        public EmulatorDeletionTask(AndroidEmulatorContext emuContext, AndroidEmulator emu) {
        	this.emuContext = emuContext;
        	this.emu = emu;
            this.listener = emuContext.getTaskListener();
        }

        public Boolean call() throws Exception {
            if (logger == null) {
                logger = listener.getLogger();
            }

            // Check whether the AVD exists
            final File homeDir = Utils.getHomeDirectory(emuContext.getSdk().getSdkHome());
            final File avdDirectory = Utils.getAvdDirectory(homeDir, emu.getAvdName());
            final boolean emulatorExists = avdDirectory.exists();
            if (!emulatorExists) {
                AndroidEmulator.log(logger, Messages.AVD_DIRECTORY_NOT_FOUND(avdDirectory));
                return false;
            }

            // Recursively delete the contents
    		Utils.delFile(avdDirectory);

    		// Delete the metadata file
    		Utils.getAvdMetadataFile(androidSdk.getSdkHome(), emu.getAvdName()).delete();

            // Success!
            return true;
        }

    }
    
    /** Task that will block until it can either connect to a port on localhost, or it times-out. */
    public static final class LocalPortOpenTask implements Callable<Boolean, InterruptedException> {

        private static final long serialVersionUID = 1L;

        private final int port;
        private final int timeout;

        /**
         * @param port The local TCP port to attempt to connect to.
         * @param timeout How long to keep trying (in milliseconds) before giving up.
         */
        public LocalPortOpenTask(int port, int timeout) {
            this.port = port;
            this.timeout = timeout;
        }

        public Boolean call() throws InterruptedException {
            final long start = System.currentTimeMillis();

            while (System.currentTimeMillis() < start + timeout) {
                try {
                    Socket socket = new Socket("127.0.0.1", port);
                    socket.getOutputStream();
                    socket.close();
                    return true;
                } catch (IOException ignore) {}

                Thread.sleep(1000);
            }

            return false;
        }
    }
}
