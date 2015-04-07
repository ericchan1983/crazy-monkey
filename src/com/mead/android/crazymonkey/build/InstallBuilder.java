package com.mead.android.crazymonkey.build;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.Pattern;

import com.mead.android.crazymonkey.AndroidEmulator;
import com.mead.android.crazymonkey.AndroidEmulatorContext;
import com.mead.android.crazymonkey.CrazyMonkeyBuild;
import com.mead.android.crazymonkey.Messages;
import com.mead.android.crazymonkey.StreamTaskListener;
import com.mead.android.crazymonkey.model.AppRunner;
import com.mead.android.crazymonkey.model.Task;
import com.mead.android.crazymonkey.process.ForkOutputStream;
import com.mead.android.crazymonkey.sdk.AndroidSdk;
import com.mead.android.crazymonkey.sdk.Tool;
import com.mead.android.crazymonkey.util.Utils;

public class InstallBuilder extends Builder {

	private String apkFile;

	private boolean uninstallFirst;

	private boolean failOnInstallFailure;

	private InstallBuilder(String apkFile, boolean uninstallFirst, boolean failOnInstallFailure) {
		super();
		this.apkFile = apkFile;
		this.uninstallFirst = uninstallFirst;
		this.failOnInstallFailure = failOnInstallFailure;
	}

	@Override
	public boolean perform(CrazyMonkeyBuild build, AndroidSdk androidSdk, AndroidEmulator emulator, AndroidEmulatorContext emuContext,
			StreamTaskListener taskListener, String successText) throws IOException, InterruptedException {
		final PrintStream logger = taskListener.getLogger();
		if (Utils.fixEmptyAndTrim(apkFile) == null) {
			AndroidEmulator.log(logger, Messages.APK_NOT_SPECIFIED());
			return false;
		}

		File apkFile = new File(build.getApkFilePath(), this.apkFile);

		if (!apkFile.exists()) {
			AndroidEmulator.log(logger, Messages.APK_NOT_FOUND(apkFile.getAbsoluteFile()));
			return false;
		}

		final String deviceIdentifier = String.format("-s %s", emuContext.getSerial());

		if (this.isUninstallFirst()) {
			uninstallApk(logger, androidSdk, deviceIdentifier, apkFile);
		}
		// Execute installation
		AndroidEmulator.log(logger, Messages.INSTALLING_APK(apkFile.getName()));

		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
		ForkOutputStream forkStream = new ForkOutputStream(logger, stdout);
		String args = String.format("%s install -r \"%s\"", deviceIdentifier, apkFile.getName());
		Utils.runAndroidTool(forkStream, logger, androidSdk, Tool.ADB, args, new File(apkFile.getParent()));

		Pattern p = Pattern.compile("^Success$", Pattern.MULTILINE);
		boolean success = p.matcher(stdout.toString()).find();
		if (!success && failOnInstallFailure) {
			return false;
		}
		return true;
	}

	public static Builder getInstance(Task task) {
		AppRunner appRunner = task.getAppRunner();
		Builder builder = new InstallBuilder(appRunner.getAppId(), false, true);
		return builder;
	}
	
	public static Builder getTestInstance(Task task) {
		AppRunner appRunner = task.getAppRunner();
		String apkName = appRunner.getAppId().substring(0, appRunner.getAppId().length() - 4) + "_test.apk";
		Builder builder = new InstallBuilder(apkName, false, true);
		return builder;
	}

	public String getApkFile() {
		return apkFile;
	}

	public boolean isUninstallFirst() {
		return uninstallFirst;
	}

	public boolean isFailOnInstallFailure() {
		return failOnInstallFailure;
	}

}
