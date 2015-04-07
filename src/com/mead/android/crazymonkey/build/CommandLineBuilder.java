package com.mead.android.crazymonkey.build;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.mead.android.crazymonkey.AndroidEmulator;
import com.mead.android.crazymonkey.AndroidEmulatorContext;
import com.mead.android.crazymonkey.CrazyMonkeyBuild;
import com.mead.android.crazymonkey.StreamTaskListener;
import com.mead.android.crazymonkey.process.ForkOutputStream;
import com.mead.android.crazymonkey.process.LocalProc;
import com.mead.android.crazymonkey.process.ProcStarter;
import com.mead.android.crazymonkey.sdk.AndroidSdk;
import com.mead.android.crazymonkey.util.Utils;

public abstract class CommandLineBuilder extends Builder{
	
	protected String script;
	
	protected List<String> args;
	
	public CommandLineBuilder(String script, List<String> args) {
		this.script = script;
		this.args = args;
	}
	
	public abstract String[] buildCommandLine();
	
	public boolean perform(CrazyMonkeyBuild build, AndroidSdk androidSdk, AndroidEmulator emulator, AndroidEmulatorContext emuContext,
			StreamTaskListener taskListener, String successText) throws IOException, InterruptedException {

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
			
			if (androidSdk.hasKnownHome()) {
				buildEnvironment.put("ANDROID_SDK_HOME", androidSdk.getSdkHome());
			}
			if (Utils.isUnix()) {
				buildEnvironment.put("LD_LIBRARY_PATH", String.format("%s/tools/lib", androidSdk.getSdkRoot()));
			}
			buildEnvironment.put("CRAZY_MONKEY_HOME", build.getCrazyMonkeyHome());
			
			ByteArrayOutputStream emulatorOutput = new ByteArrayOutputStream();
			ForkOutputStream emulatorLogger = new ForkOutputStream(logger, emulatorOutput);
			
			LocalProc localProc = new ProcStarter().cmds(buildCommandLine()).stdout(emulatorLogger).envs(buildEnvironment).start();
			int r = localProc.join();
			
			String result = emulatorOutput.toString();
			if (r == 0 && result != null && (result.contains("OK (1 test)") || result.contains(successText))) {
				return true;
			} else {
				return false;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			AndroidEmulator.log(logger, String.format("Run the batch file '%s' failed.", script));
			return false;
		}
	}
}
