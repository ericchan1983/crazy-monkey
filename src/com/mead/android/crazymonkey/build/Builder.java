package com.mead.android.crazymonkey.build;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mead.android.crazymonkey.AndroidEmulator;
import com.mead.android.crazymonkey.AndroidEmulatorContext;
import com.mead.android.crazymonkey.CrazyMonkeyBuild;
import com.mead.android.crazymonkey.Messages;
import com.mead.android.crazymonkey.StreamTaskListener;
import com.mead.android.crazymonkey.process.ForkOutputStream;
import com.mead.android.crazymonkey.sdk.AndroidSdk;
import com.mead.android.crazymonkey.sdk.Tool;
import com.mead.android.crazymonkey.util.Utils;

public abstract class Builder {
	
	public abstract boolean perform(CrazyMonkeyBuild build, AndroidSdk androidSdk, AndroidEmulator emulator, AndroidEmulatorContext emuContext, StreamTaskListener taskListener, String successText)  throws IOException, InterruptedException;
	
	/**
	 * Uninstalls the Android package corresponding to the given APK file from an Android device.
	 * 
	 * @param build The build for which we should uninstall the package.
	 * @param launcher The launcher for the remote node.
	 * @param logger Where log output should be redirected to.
	 * @param androidSdk The Android SDK to use.
	 * @param deviceIdentifier The device from which the package should be removed.
	 * @param apkPath The path to the APK file.
	 * @return {@code true} iff uninstallation completed successfully.
	 * @throws IOException If execution failed.
	 * @throws InterruptedException If execution failed.
	 */
	protected static boolean uninstallApk(PrintStream logger, AndroidSdk androidSdk, String deviceIdentifier, File apkPath)
			throws IOException, InterruptedException {
		// Get package ID to uninstall
		String packageId = getPackageIdForApk(logger, androidSdk, apkPath);
		return uninstallApk(logger, androidSdk, deviceIdentifier, packageId);
	}

	/**
	 * Uninstalls the given Android package ID from the given Android device.
	 */
	protected static boolean uninstallApk(PrintStream logger, AndroidSdk androidSdk, String deviceIdentifier, String packageId)
			throws IOException, InterruptedException {

		AndroidEmulator.log(logger, Messages.UNINSTALLING_APK(packageId));

		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
		ForkOutputStream forkStream = new ForkOutputStream(logger, stdout);
		String adbArgs = String.format("%s uninstall %s", deviceIdentifier, packageId);
		Utils.runAndroidTool(forkStream, logger, androidSdk, Tool.ADB, adbArgs, null);

		// The package manager simply returns "Success" or "Failure" on stdout
		return stdout.toString().contains("Success");
	}

	/**
	 * Determines the package ID of an APK file.
	 * 
	 */
	private static String getPackageIdForApk(PrintStream logger, AndroidSdk androidSdk, File apkPath) throws IOException,
			InterruptedException {
		// Run aapt command on given APK
		ByteArrayOutputStream aaptOutput = new ByteArrayOutputStream();
		String args = String.format("dump badging \"%s\"", apkPath.getName());
		Utils.runAndroidTool(aaptOutput, logger, androidSdk, Tool.AAPT, args, new File(apkPath.getParent()));

		// Determine package ID from aapt output
		String packageId = null;
		String aaptResult = aaptOutput.toString();
		if (aaptResult.length() > 0) {
			Matcher matcher = Pattern.compile("package: +name='([^']+)'").matcher(aaptResult);
			if (matcher.find()) {
				packageId = matcher.group(1);
			}
		}

		return packageId;
	}
}
