package com.mead.android.crazymonkey;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.mead.android.crazymonkey.build.Builder;
import com.mead.android.crazymonkey.build.CommandLineBuilder;
import com.mead.android.crazymonkey.model.Task;
import com.mead.android.crazymonkey.model.Task.STATUS;
import com.mead.android.crazymonkey.process.ForkOutputStream;
import com.mead.android.crazymonkey.process.ProcStarter;
import com.mead.android.crazymonkey.sdk.AndroidSdk;
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
		
		String script = build.getTestScriptPath() + "//genymotion_start.bat";
		if (Utils.isUnix()) {
			script = build.getTestScriptPath() + "//genymotion_start.sh";
		}

		List<String> args = new ArrayList<String>();
		args.add(task.getEmulator().getAvdName());

		CommandLineBuilder builder = this.getBuilder(script, args);

		final PrintStream logger = taskListener.getLogger();
		File file = new File(script);

		if (!file.exists()) {
			AndroidEmulator.log(logger, String.format("The script file '%s' is not existing.", script));
			return false;
		}
		
		try {
			Map<String, String> buildEnvironment = new TreeMap<String, String>();
			Map<String, String> sysEnv = System.getenv();
			for (String key : sysEnv.keySet()) {
				buildEnvironment.put(key, sysEnv.get(key));
			}
			buildEnvironment.put("CRAZY_MONKEY_HOME", build.getCrazyMonkeyHome());
			ByteArrayOutputStream emulatorOutput = new ByteArrayOutputStream();
			ForkOutputStream emulatorLogger = new ForkOutputStream(logger, emulatorOutput);
			new ProcStarter().cmds(builder.buildCommandLine()).stdout(emulatorLogger).envs(buildEnvironment).start();
		} catch (IOException e) {
			e.printStackTrace();
			AndroidEmulator.log(logger, String.format("Run the batch file '%s' failed.", script));
			return false;
		}
		
		boolean connResult = connectGenymotion();
		return connResult;
		
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
			log(logger, String.format("Genymotion is ready for use."));
			this.getContext().setSerial(getSerialForGenyMotion("genymotion_" + task.getEmulator().getAvdName() + ".ini"));
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
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public String getSerialForGenyMotion(String fileName) throws IOException {
		BufferedReader reader = null;
		String line = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.build.getUserDataPath() + "//" + fileName)));
			line = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		System.out.println("sn = " + line);
		return line;
	}
}
