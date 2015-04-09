package com.mead.android.crazymonkey;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import com.mead.android.crazymonkey.build.Builder;
import com.mead.android.crazymonkey.build.CommandLineBuilder;
import com.mead.android.crazymonkey.model.Task;
import com.mead.android.crazymonkey.model.Task.STATUS;
import com.mead.android.crazymonkey.process.ForkOutputStream;
import com.mead.android.crazymonkey.process.LocalProc;
import com.mead.android.crazymonkey.process.ProcStarter;
import com.mead.android.crazymonkey.sdk.AndroidSdk;
import com.mead.android.crazymonkey.sdk.Tool;
import com.mead.android.crazymonkey.util.Utils;

public class GenymotionRunner extends AbstractRunner {

	public GenymotionRunner(CrazyMonkeyBuild build, Task task, AndroidSdk androidSdk, StreamTaskListener taskListener) {
		super(build, task, androidSdk, taskListener);
	}

	@Override
	public boolean startUp() throws IOException, InterruptedException {

		if (logger == null) {
			logger = taskListener.getLogger();
		}
		AndroidEmulator emuConfig = task.getEmulator();
		final AndroidEmulatorContext emu = new AndroidEmulatorContext(build, "", androidSdk, taskListener);
		this.setContext(emu);

		final long bootTime = System.currentTimeMillis();

		// play the genymotion emulator
		boolean playSuccess = playGenymotion(emu);
		if (!playSuccess) {
			log(logger, "Play the genymotion failed.");
			task.setStatus(STATUS.NOT_BUILT);
			return false;
		}

		// connect to the genymotion and write the serial to a file "./userdata/genymotion_Android_Monkey_0.ini"
		boolean connectSuccess = connectGenymotion();
		if (!connectSuccess) {
			log(logger, "Could not get the serial no and write to a file.");
			task.setStatus(STATUS.NOT_BUILT);
			return false;
		}

		// read the serial from the sepcified file
		final String fileName = "genymotion_" + task.getEmulator().getAvdName() + ".ini";
		String serial = getSerialForGenyMotion(fileName);
		emu.setSerial(serial);
		if (serial == null) {
			log(logger, String.format("Could not get the serial no from the file %s", fileName));
			task.setStatus(STATUS.NOT_BUILT);
			return false;
		}
		
		// try to connect to the emulator
		int result = emu.getToolProcStarter(Tool.ADB, "connect " + emu.getSerial()).join();
		if (result != 0) {
			log(logger, Messages.CANNOT_CONNECT_TO_EMULATOR());
			task.setStatus(STATUS.NOT_BUILT);
			return false;
		}

		// Monitor device for boot completion signal
		boolean ignoreProcess = true;
		log(logger, Messages.WAITING_FOR_BOOT_COMPLETION());
		int bootTimeout = CrazyMonkeyBuild.BOOT_COMPLETE_TIMEOUT_MS * 2;
		boolean bootSucceeded = waitForBootCompletion(ignoreProcess, bootTimeout, emuConfig, emu);
		if (!bootSucceeded) {
			if ((System.currentTimeMillis() - bootTime) < bootTimeout) {
				log(logger, Messages.EMULATOR_STOPPED_DURING_BOOT());
			} else {
				log(logger, Messages.BOOT_COMPLETION_TIMED_OUT(bootTimeout / 1000));
			}
			task.setStatus(STATUS.NOT_BUILT);
			return false;
		}

		// Done!
        final long bootCompleteTime = System.currentTimeMillis();
        log(logger, Messages.EMULATOR_IS_READY((bootCompleteTime - bootTime) / 1000));
        
		return true;
	}

	public boolean playGenymotion(final AndroidEmulatorContext emu) throws IOException, InterruptedException {

		String script = build.getTestScriptPath() + "//genymotion_start.bat";
		if (Utils.isUnix()) {
			script = build.getTestScriptPath() + "//genymotion_start.sh";
		}

		File file = new File(script);
		if (!file.exists()) {
			AndroidEmulator.log(logger, String.format("The script file '%s' is not existing.", script));
			return false;
		}

		try {
			LocalProc adbStart = emu.getToolProcStarter(Tool.ADB, "start-server").stdout(logger).start();
			adbStart.joinWithTimeout(5L, TimeUnit.SECONDS, taskListener);
			LocalProc adbStart2 = emu.getToolProcStarter(Tool.ADB, "start-server").stdout(logger).start();
			adbStart2.joinWithTimeout(5L, TimeUnit.SECONDS, taskListener);
			
			Map<String, String> buildEnvironment = new TreeMap<String, String>();
			Map<String, String> sysEnv = System.getenv();
			for (String key : sysEnv.keySet()) {
				buildEnvironment.put(key, sysEnv.get(key));
			}
			buildEnvironment.put("CRAZY_MONKEY_HOME", build.getCrazyMonkeyHome());
			ByteArrayOutputStream emulatorOutput = new ByteArrayOutputStream();
			ForkOutputStream emulatorLogger = new ForkOutputStream(logger, emulatorOutput);
			
			List<String> args = new ArrayList<String>();
			args.add(task.getEmulator().getAvdName());
			CommandLineBuilder builder = this.getBuilder(script, args);
			final LocalProc emulatorProcess = new ProcStarter().cmds(builder.buildCommandLine()).stdout(emulatorLogger)
					.envs(buildEnvironment).start();
			emu.setEmulatorProcess(emulatorProcess);

		} catch (IOException e) {
			e.printStackTrace();
			AndroidEmulator.log(logger, String.format("Run the batch file '%s' failed.", script));
			return false;
		}
		return true;
	}

	public boolean connectGenymotion() throws IOException, InterruptedException {
		boolean result = false;

		if (logger == null) {
			logger = taskListener.getLogger();
		}

		String script = build.getTestScriptPath() + "//genymotion_get_ip.bat";
		if (Utils.isUnix()) {
			script = build.getTestScriptPath() + "//genymotion_get_ip.sh";
		}

		List<String> args = new ArrayList<String>();
		args.add(task.getEmulator().getAvdName());
		Builder builder = this.getBuilder(script, args);
		result = builder.perform(build, androidSdk, task.getEmulator(), context, taskListener, "Success");

		if (!result) {
			log(logger, String.format("Start the geny motion %s via '%s' failed.", task.getEmulator().getAvdName(), script));
			task.setStatus(STATUS.NOT_BUILT);
		} else {
			log(logger, String.format("Start the genymotion %s via '%s' scussfully.", task.getEmulator().getAvdName(), script));
		}
		return result;
	}

	@Override
	public boolean tearDown() throws IOException, InterruptedException {
		boolean result = false;
		try {
			if (logger == null) {
				logger = taskListener.getLogger();
			}

			String script = build.getTestScriptPath() + "//genymotion_stop.bat";
			if (Utils.isUnix()) {
				script = build.getTestScriptPath() + "//genymotion_stop.sh";
			}

			List<String> args = new ArrayList<String>();
			args.add(task.getEmulator().getAvdName());
			Builder builder = this.getBuilder(script, args);
			result = builder.perform(build, androidSdk, task.getEmulator(), context, taskListener, "Success");

			if (!result) {
				log(logger, String.format("Kill the geny motion via '%s' failed.", script));
			} else {
				log(logger, String.format("Kill the geny motion via '%s' scussfully.", script));
			}
		} finally {
			build.freeEmulator(task.getEmulator().getAvdName());
		}
		return result;
	}

	/**
	 * Get the serial
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public String getSerialForGenyMotion(String fileName) {
		BufferedReader reader = null;
		String line = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.build.getUserDataPath() + "//" + fileName)));
			line = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return line;
	}
}
