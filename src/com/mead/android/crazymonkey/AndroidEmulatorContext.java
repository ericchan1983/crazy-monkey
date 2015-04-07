package com.mead.android.crazymonkey;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;
import java.util.TreeMap;

import com.mead.android.crazymonkey.process.ArgumentListBuilder;
import com.mead.android.crazymonkey.process.LocalProc;
import com.mead.android.crazymonkey.process.ProcStarter;
import com.mead.android.crazymonkey.sdk.AndroidSdk;
import com.mead.android.crazymonkey.sdk.Tool;
import com.mead.android.crazymonkey.util.Utils;

public class AndroidEmulatorContext {

	public static final String EMULATOR_PREFIX = "emulator-";
	 /** Interval during which an emulator command should complete. */
    public static final int EMULATOR_COMMAND_TIMEOUT_MS = 5 * 60 * 1000;

	private int adbPort, userPort, adbServerPort;
	private String serial;
	private LocalProc emulatorProcess;
	private AndroidSdk sdk;
	private StreamTaskListener taskListener;
	private CrazyMonkeyBuild build;

	public AndroidEmulatorContext(CrazyMonkeyBuild build, int[] ports, AndroidSdk sdk, StreamTaskListener taskListener) {
		super();
		this.build = build;
		this.userPort = ports[0];
		this.adbPort = ports[1];
		//this.adbServerPort = ports[2];
		this.serial = EMULATOR_PREFIX + userPort;
		this.sdk = sdk;
		this.taskListener = taskListener;
	}

	/**
	 * Generates a ready-to-use ProcStarter for one of the Android SDK tools, based on the current context.
	 * 
	 * @param tool The Android tool to run.
	 * @param args Any extra arguments for the command.
	 * @return A ready ProcStarter
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public ProcStarter getToolProcStarter(Tool tool, String args) throws IOException, InterruptedException {
		return getProcStarter(Utils.getToolCommand(sdk, Utils.isUnix(), tool, args));
	}
	
	
	/**
	 * Generates a ready-to-use ProcStarter for one of the Android SDK tools, based on the current context.
	 * 
	 * @param tool The Android tool to run.
	 * @param args Any extra arguments for the command.
	 * @return A ready ProcStarter
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public ProcStarter getToolProcStarter(Tool tool, String program, String args) throws IOException, InterruptedException {
		return getProcStarter(Utils.getToolCommand(sdk, Utils.isUnix(), tool, program, args));
	}
	
	/**
	 * Generates a ready-to-use ArgumentListBuilder for one of the Android SDK tools, based on the current
	 * context.
	 * 
	 * @param tool The Android tool to run.
	 * @param args Any extra arguments for the command.
	 * @return Arguments including the full path to the SDK and any extra Windows stuff required.
	 */
	public ArgumentListBuilder getToolCommand(Tool tool, String args) {
		return Utils.getToolCommand(sdk, Utils.isUnix(), tool, args);
	}
	
	/**
	 * 
	 * Sets up a standard {@link ProcStarter} for the current adb environment,
	 * ready to execute the given command.
	 * 
	 * @param command What command to run
	 * @return A ready ProcStarter
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public ProcStarter getProcStarter(ArgumentListBuilder command)
			throws IOException, InterruptedException {
		return getProcStarter().cmds(command);
	}

	/**
	 * Sets up a standard {@link ProcStarter} for the current context. 
	 * 
	 * @return A ready ProcStarter
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public ProcStarter getProcStarter() throws IOException, InterruptedException {
		Map<String, String> buildEnvironment = new TreeMap<String, String>();
		
		Map<String, String> sysEnv = System.getenv();
		
		for (String key : sysEnv.keySet()) {
			buildEnvironment.put(key, sysEnv.get(key));
		}
		
		if (sdk.hasKnownHome()) {
			buildEnvironment.put("ANDROID_SDK_HOME", sdk.getSdkHome());
		}
		
		if (Utils.isUnix()) {
			buildEnvironment.put("LD_LIBRARY_PATH", String.format("%s/tools/lib", sdk.getSdkRoot()));
		}
		
		buildEnvironment.put("CRAZY_MONKEY_HOME", build.getCrazyMonkeyHome());
		
		return new ProcStarter().stdout(this.getTaskListener().getLogger()).stderr(this.getTaskListener().getLogger()).envs(buildEnvironment);
	}
	
	/**
	 * Sends a user command to the running emulator via its telnet interface.<br>
	 * Execution will be cancelled if it takes longer than
	 * {@link #EMULATOR_COMMAND_TIMEOUT_MS}.
	 * 
	 * @param command The command to execute on the emulator's telnet interface.
	 * @return Whether sending the command succeeded.
	 */
	public boolean sendCommand(final String command) {
		return sendCommand(command, EMULATOR_COMMAND_TIMEOUT_MS);
	}
	
	/**
	 * Sends a user command to the running emulator via its telnet interface.<br>
	 * Execution will be cancelled if it takes longer than timeout ms.
	 * 
	 * @param command The command to execute on the emulator's telnet interface.
	 * @param timeout The command's timeout, in ms.
	 * @return Whether sending the command succeeded.
	 */
	public boolean sendCommand(final String command, int timeout) {
		return Utils.sendEmulatorCommand(build, logger(), userPort, command, timeout);
	}
	
	
	public int getAdbPort() {
		return adbPort;
	}

	public int getAdbServerPort() {
		return adbServerPort;
	}

	public LocalProc getEmulatorProcess() {
		return emulatorProcess;
	}
	
	public LocalProc getProcess() {
		return emulatorProcess;
	}

	public AndroidSdk getSdk() {
		return sdk;
	}

	public String getSerial() {
		return serial;
	}

	public StreamTaskListener getTaskListener() {
		return taskListener;
	}

	public int getUserPort() {
		return userPort;
	}

	public void setAdbPort(int adbPort) {
		this.adbPort = adbPort;
	}

	public void setAdbServerPort(int adbServerPort) {
		this.adbServerPort = adbServerPort;
	}

	public void setEmulatorProcess(LocalProc emulatorProcess) {
		this.emulatorProcess = emulatorProcess;
	}

	public void setSdk(AndroidSdk sdk) {
		this.sdk = sdk;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public void setTaskListener(StreamTaskListener taskListener) {
		this.taskListener = taskListener;
	}

	public void setUserPort(int userPort) {
		this.userPort = userPort;
	}

	public PrintStream logger() {
		return taskListener.getLogger();
	}

	public CrazyMonkeyBuild getBuild() {
		return build;
	}

	public void setBuild(CrazyMonkeyBuild build) {
		this.build = build;
	}
	
}
