package com.mead.android.crazymonkey;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.mead.android.crazymonkey.build.Builder;
import com.mead.android.crazymonkey.build.CommandLineBuilder;
import com.mead.android.crazymonkey.build.InstallBuilder;
import com.mead.android.crazymonkey.build.RunBatBuilder;
import com.mead.android.crazymonkey.build.RunShellBuilder;
import com.mead.android.crazymonkey.model.Task;
import com.mead.android.crazymonkey.model.Task.STATUS;
import com.mead.android.crazymonkey.persistence.MongoTask;
import com.mead.android.crazymonkey.persistence.TaskDAO;
import com.mead.android.crazymonkey.process.ArgumentListBuilder;
import com.mead.android.crazymonkey.process.LocalProc;
import com.mead.android.crazymonkey.sdk.AndroidSdk;
import com.mead.android.crazymonkey.sdk.Tool;
import com.mead.android.crazymonkey.util.Utils;

public abstract class AbstractRunner implements java.util.concurrent.Callable<Task> {

	protected static ObjectMapper objectMapper = new ObjectMapper();

	protected CrazyMonkeyBuild build;

	protected Task task;

	protected AndroidSdk androidSdk;

	protected StreamTaskListener taskListener;

	protected AndroidEmulatorContext context;

	protected PrintStream logger;

	public AbstractRunner(CrazyMonkeyBuild build, Task task, AndroidSdk androidSdk, StreamTaskListener taskListener) {
		super();
		this.build = build;
		this.task = task;
		this.androidSdk = androidSdk;
		this.taskListener = taskListener;
	}

	public abstract boolean startUp(boolean isRestoreFactory) throws IOException, InterruptedException;

	public abstract boolean tearDown() throws IOException, InterruptedException;

	@Override
	public Task call() {
		try {
			// start up the env
			Thread.sleep(3000);
			boolean isRunEmulatorSuccess = startUp(true);
			
			if (!isRunEmulatorSuccess) {
				return task;
			}
			
			Thread.sleep(build.getConfigPhoneDelay() * 1000);
			boolean writeBuildProp = writeBuildProp();
			if (!writeBuildProp) {
				return task;
			}
			
			isRunEmulatorSuccess = startUp(false);
			
			if (isRunEmulatorSuccess) {
				// configure the phone
				Thread.sleep(build.getConfigPhoneDelay() * 1000);
				boolean configPhoneSuccess = configPhoneInfo();
				
				if (configPhoneSuccess) {
					
					Thread.sleep(3000);
					// connect to vpn if necessary
					if (build.isConnectToVPN()) {
						if (!switchVPN()) {
							return task;
						}
					}
					
					boolean result = false;
					// install the apk file
					Thread.sleep(build.getInstallApkDelay() * 1000);
					Builder installBuilder = InstallBuilder.getInstance(task);
					
					synchronized (this) {
						result = installBuilder.perform(build, androidSdk, task.getEmulator(), context, taskListener, "Success");
					}
					if (!result) {
						log(logger, String.format("Failed to intsall the apk '%s'.", task.getAppRunner().getAppId()));
						task.setStatus(STATUS.FAILURE);
					} else {
						// install the test apk file
						Thread.sleep(build.getInstallApkDelay() * 1000);
						Builder installTestApk = InstallBuilder.getTestInstance(task);
						String apkName = task.getAppRunner().getAppId().substring(0, task.getAppRunner().getAppId().length() - 4) + "_test.apk";

						synchronized (this) {
							result = installTestApk.perform(build, androidSdk, task.getEmulator(), context, taskListener, "Success");
						}
						if (!result) {
							log(logger, String.format("Failed to intsall the test apk '%s'.", apkName));
						}
						
						// run the script 
						Thread.sleep(build.getRunScriptDelay() * 1000);
						runScripts();
					}
				}
			}
		} catch (InterruptedException | IOException e) {
			task.setStatus(STATUS.FAILURE);
			String msg = String.format("The task '%d' is interrputed. ", task.getAppRunner().getAppId());
			log(logger, msg);
			System.out.println("[" + new Date() + "]" + msg);
		} finally {
			try {
				tearDown();
				
				TaskDAO taskDAO = new MongoTask(build);
				taskDAO.updateTask(task);
				
				long executeTime = (System.currentTimeMillis() - task.getAssignTime().getTime()) / 1000;
				String result = String.format("The monkey task '%s' has been completed %s in %d seconds. ",
						task.getId(), task.getStatus(), executeTime);
				
				AndroidEmulator.log(logger,result);
		        logger.flush();
		        logger.close();
		        
		        System.out.println("[" + new Date() + "] - " + result);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		return task;
	}

	/**
	 * Checks whether the emulator running on the given port has finished booting yet, or times out.
	 * 
	 * @param ignoreProcess Whether to bypass checking that the process is alive (e.g. on Windows).
	 * @param timeout How long to keep trying (in milliseconds) before giving up.
	 * @param emu The emulator context
	 * @return <code>true</code> if the emulator has booted, <code>false</code> if we timed-out.
	 */
	public boolean waitForBootCompletion(final boolean ignoreProcess, final int timeout, AndroidEmulator config, AndroidEmulatorContext emu) {
		long start = System.currentTimeMillis();
		int sleep = timeout / (int) (Math.sqrt(timeout / 1000) * 2);

		int apiLevel = config.getOsVersion().getSdkLevel();

		// Other tools use the "bootanim" variant, which supposedly signifies the system has booted a bit further;
		// though this doesn't appear to be available on Android 1.5, while it should work fine on Android 1.6+
		final boolean isOldApi = apiLevel > 0 && apiLevel < 4;
		final String cmd = isOldApi ? "dev.bootcomplete" : "sys.boot_completed";
		final String expectedAnswer = "1";
		final String args = String.format("-s %s shell getprop %s", emu.getSerial(), cmd);
		ArgumentListBuilder bootCheckCmd = emu.getToolCommand(Tool.ADB, args);

		try {
			final long adbTimeout = timeout / 8;
			while (System.currentTimeMillis() < start + timeout && (ignoreProcess || emu.getProcess().isAlive())) {
				ByteArrayOutputStream stream = new ByteArrayOutputStream(16);

				// Run "getprop", timing-out in case adb hangs
				LocalProc proc = emu.getProcStarter(bootCheckCmd).stdout(stream).start();
				int retVal = proc.joinWithTimeout(adbTimeout, TimeUnit.MILLISECONDS, emu.getTaskListener());
				if (retVal == 0) {
					// If boot is complete, our work here is done
					String result = stream.toString().trim();
					if (result.equals(expectedAnswer)) {
						return true;
					}
				}

				// "getprop" failed, so sleep and try again later
				Thread.sleep(sleep);
			}
		} catch (InterruptedException ex) {
			log(emu.logger(), Messages.INTERRUPTED_DURING_BOOT_COMPLETION());
		} catch (IOException ex) {
			log(emu.logger(), Messages.COULD_NOT_CHECK_BOOT_COMPLETION());
			ex.printStackTrace(emu.logger());
		}

		return false;
	}
	
	public boolean runScripts() throws IOException, InterruptedException {
		boolean result = false;
		task.startTask();
		String script = build.getApkFilePath() + "//" + task.getAppRunner().getScriptName();

		if (Utils.isUnix()) {
			script += ".sh";
		} else {
			script += ".bat";
		}

		List<String> args = new ArrayList<String>();
		args.add(context.getSerial());
		args.add("backup_" + task.getJobId() + "_" + task.getPhone().getGetDeviceId() + ".tar.gz");

		Builder builder = getBuilder(script, args);
		int tryNum = 2;
		result = runTestCase(script, builder, tryNum);
		return result;
	}

	public boolean runTestCase(String script, Builder builder, int tryNum) throws IOException, InterruptedException {
		boolean result;
		result = builder.perform(build, androidSdk, task.getEmulator(), context, taskListener, "Monkey success.");

		if (!result) {
			log(logger, String.format("Run the script '%s' failed.", script));
			if ((System.currentTimeMillis() - task.getExecStartTime().getTime()) / 1000 >= build.getRunScriptTimeout()) {
				task.compelteTask(STATUS.NOT_COMPLETE);
			} else {
				if (tryNum >= 1) {
					result = runTestCase(script, builder, --tryNum);
				} else {
					task.compelteTask(STATUS.NOT_COMPLETE);
				}
			}
		} else {
			log(logger, String.format("Run the script '%s' scussfully.", script));
			task.compelteTask(STATUS.SUCCESS);
		}
		return result;
	}

	public CommandLineBuilder getBuilder(String script, List<String> args) {
		CommandLineBuilder builder = null;
		if (!Utils.isUnix()) {
			builder = new RunBatBuilder(script, args);
		} else {
			builder = new RunShellBuilder(script, args);
		}
		return builder;
	}

	public boolean configPhoneInfo() throws IOException, InterruptedException {
		if (logger == null) {
			logger = taskListener.getLogger();
		}
		writeDeviceTxt();
		
		String script = build.getTestScriptPath() + "//config_phone.bat";
		if (Utils.isUnix()) {
			script = build.getTestScriptPath() + "//config_phone.sh";
		}
		List<String> args = new ArrayList<String>();
		args.add(context.getSerial());
		args.add(task.getEmulator().getAvdName());
		
		Builder builder = this.getBuilder(script, args);
		boolean result = builder.perform(build, androidSdk, task.getEmulator(), context, taskListener, "OK (1 test)");
		if (!result) {
			log(logger, String.format("Config the phone information '%s' failed.", script));
			task.setStatus(STATUS.NOT_BUILT);
		} else {
			log(logger, String.format("Config the phone information '%s' scussfully.", script));
		}
		return result;
	}
	
	public boolean writeBuildProp() throws IOException, InterruptedException {
		if (logger == null) {
			logger = taskListener.getLogger();
		}
		
		writeDeviceTxt();

		String script = build.getTestScriptPath() + "//config_build_prop.bat";
		if (Utils.isUnix()) {
			script = build.getTestScriptPath() + "//config_build_prop.sh";
		}
		List<String> args = new ArrayList<String>();
		args.add(String.valueOf(context.getSerial()));
		args.add(task.getEmulator().getAvdName());

		Builder builder = this.getBuilder(script, args);
		boolean result = builder.perform(build, androidSdk, task.getEmulator(), context, taskListener, "Success.");
		if (!result) {
			log(logger, String.format("Write the build prop '%s' failed.", script));
			task.setStatus(STATUS.NOT_BUILT);
		} else {
			log(logger, String.format("Write the build prop '%s' scussfully.", script));
		}
		return result;
	}
	
	public boolean switchVPN() throws IOException, InterruptedException {
		if (logger == null) {
			logger = taskListener.getLogger();
		}
		String script = build.getTestScriptPath() + "//connect_vpn.bat";
		if (Utils.isUnix()) {
			script = build.getTestScriptPath() + "//connect_vpn.sh";
		}
		List<String> args = new ArrayList<String>();
		args.add(String.valueOf(context.getSerial()));
		Builder builder = this.getBuilder(script, args);
		boolean result = builder.perform(build, androidSdk, task.getEmulator(), context, taskListener, "Monkey success.");
		if (!result) {
			log(logger, String.format("Connect to VPN '%s' failed.", script));
			task.setStatus(STATUS.NOT_BUILT);
		} else {
			log(logger, String.format("Connect to VPN '%s' scussfully.", script));
		}
		return result;
	}

	private void writeDeviceTxt() throws IOException, FileNotFoundException, JsonGenerationException, JsonMappingException {
		File f = new File(build.getUserDataPath() + "//xposeDevice_" + task.getEmulator().getAvdName() + ".txt");
		f.getParentFile().mkdirs();
		f.createNewFile();
		FileOutputStream file = new FileOutputStream(f);
		objectMapper.writeValue(file, task.getPhone());
	}

	/** Helper method for writing to the build log in a consistent manner. */
	public synchronized static void log(final PrintStream logger, final String message) {
		log(logger, message, false);
	}

	/** Helper method for writing to the build log in a consistent manner. */
	public synchronized static void log(final PrintStream logger, final String message, final Throwable t) {
		log(logger, message, false);
		StringWriter s = new StringWriter();
		t.printStackTrace(new PrintWriter(s));
		log(logger, s.toString(), false);
	}

	/** Helper method for writing to the build log in a consistent manner. */
	public synchronized static void log(final PrintStream logger, String message, boolean indent) {
		logger.print("[" + new Date() + "] ");
		if (indent) {
			message = '\t' + message.replace("\n", "\n\t");
		} else if (message.length() > 0) {
			logger.print("[android] ");
		}
		logger.println(message);
	}

	public CrazyMonkeyBuild getBuild() {
		return build;
	}

	public AndroidEmulatorContext getContext() {
		return context;
	}

	public AndroidSdk getAndroidSdk() {
		return androidSdk;
	}

	public Task getTask() {
		return task;
	}

	public StreamTaskListener gettaskListener() {
		return taskListener;
	}

	public void setBuild(CrazyMonkeyBuild build) {
		this.build = build;
	}

	public void setContext(AndroidEmulatorContext context) {
		this.context = context;
	}

	public void setAndroidSdk(AndroidSdk androidSdk) {
		this.androidSdk = androidSdk;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public void settaskListener(StreamTaskListener taskListener) {
		this.taskListener = taskListener;
	}

}
