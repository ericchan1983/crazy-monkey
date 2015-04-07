package com.mead.android.crazymonkey;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.regex.Pattern;

import com.mead.android.crazymonkey.model.HardwareProperty;
import com.mead.android.crazymonkey.sdk.AndroidSdk;
import com.mead.android.crazymonkey.sdk.Tool;
import com.mead.android.crazymonkey.util.Utils;

public class AndroidEmulator {

	private String avdName;

	// Custom emulator properties
	private AndroidPlatform osVersion;
	private ScreenDensity screenDensity;
	private ScreenResolution screenResolution;
	private String deviceLocale;
	private String targetAbi;
	private String sdCardSize;
	private String avdNameSuffix;
	private HardwareProperty[] hardwareProperties;

	// Common properties
	private boolean wipeData;
	private boolean showWindow;
	private boolean useSnapshots;

	// Advanced properties
	private boolean deleteAfterBuild;
	private int startupDelay;
	private String commandLineOptions;
	private String executable;
	
	private String androidSdkHome;

	
	public AndroidEmulator() {
		this("Android_monkey_1", "android-17", "160", "HVGA", "en_US", "200M", true, true, false, "-nocache -noaudio -noskin -gpu on", "armeabi-v7a",
				"D://tools//Android//android-sdk", "", "");
	}

	public AndroidEmulator(String avdName, boolean wipeData, boolean showWindow, boolean useSnapshots, String commandLineOptions,
			String androidSdkHome, String executable, String avdNameSuffix) {
		this.avdName = avdName;
		this.wipeData = wipeData;
		this.showWindow = showWindow;
		this.useSnapshots = useSnapshots;
		this.commandLineOptions = commandLineOptions;
		this.androidSdkHome = androidSdkHome;
		this.executable = executable;
		this.avdNameSuffix = avdNameSuffix;
	}
	
	public AndroidEmulator(String avdName, String osVersion, String screenDensity, String screenResolution,
            String deviceLocale, String sdCardSize, boolean wipeData, boolean showWindow,
            boolean useSnapshots, String commandLineOptions, String targetAbi, String androidSdkHome,
            String executable, String avdNameSuffix)
                throws IllegalArgumentException {
        if (osVersion == null || screenDensity == null || screenResolution == null) {
            throw new IllegalArgumentException("Valid OS version and screen properties must be supplied.");
        }

        this.avdName = avdName;
        
        // Normalise incoming variables
        int targetLength = osVersion.length();
        if (targetLength > 2 && osVersion.startsWith("\"") && osVersion.endsWith("\"")) {
            osVersion = osVersion.substring(1, targetLength - 1);
        }
        screenDensity = screenDensity.toLowerCase();
        if (screenResolution.matches("(?i)"+ Constants.REGEX_SCREEN_RESOLUTION_ALIAS)) {
            screenResolution = screenResolution.toUpperCase();
        } else if (screenResolution.matches("(?i)"+ Constants.REGEX_SCREEN_RESOLUTION)) {
            screenResolution = screenResolution.toLowerCase();
        }
        if (deviceLocale != null && deviceLocale.length() > 4) {
            deviceLocale = deviceLocale.substring(0, 2).toLowerCase() +"_"
                + deviceLocale.substring(3).toUpperCase();
        }

        this.osVersion = AndroidPlatform.valueOf(osVersion);
        if (this.osVersion == null) {
            throw new IllegalArgumentException(
                    "OS version not recognised: " + osVersion);
        }
        this.screenDensity = ScreenDensity.valueOf(screenDensity);
        if (this.screenDensity == null) {
            throw new IllegalArgumentException(
                    "Screen density not recognised: " + screenDensity);
        }
        this.screenResolution = ScreenResolution.valueOf(screenResolution);
        if (this.screenResolution == null) {
            throw new IllegalArgumentException(
                    "Screen resolution not recognised: " + screenResolution);
        }
        this.deviceLocale = deviceLocale;
        this.sdCardSize = sdCardSize;
        this.wipeData = wipeData;
        this.showWindow = showWindow;
        this.useSnapshots = useSnapshots;
        this.commandLineOptions = commandLineOptions;
        if (targetAbi != null && targetAbi.startsWith("default/")) {
            targetAbi = targetAbi.replace("default/", "");
        }
        this.targetAbi = targetAbi;
        this.androidSdkHome = androidSdkHome;
        this.executable = executable;
        this.avdNameSuffix = avdNameSuffix;
    }

	public String getAvdName() {
		return avdName;
	}

	public String getAvdNameSuffix() {
		return avdNameSuffix;
	}

	public String getCommandLineOptions() {
		return commandLineOptions;
	}

	public String getDeviceLocale() {
		return deviceLocale;
	}
	
	public com.mead.android.crazymonkey.sdk.Tool getExecutable() {
        for (Tool t : Tool.EMULATORS) {
            if (t.executable.equals(executable)) {
                return t;
            }
        }
        return Tool.EMULATOR;
    }

	public HardwareProperty[] getHardwareProperties() {
		return hardwareProperties;
	}

	public AndroidPlatform getOsVersion() {
		return osVersion;
	}

	public ScreenDensity getScreenDensity() {
		return screenDensity;
	}

	public ScreenResolution getScreenResolution() {
		return screenResolution;
	}

	public String getSdCardSize() {
		return sdCardSize;
	}

	public int getStartupDelay() {
		return startupDelay;
	}

	public String getTargetAbi() {
		return targetAbi;
	}

	public boolean isDeleteAfterBuild() {
		return deleteAfterBuild;
	}

	public boolean isShowWindow() {
		return showWindow;
	}

	public boolean isUseSnapshots() {
		return useSnapshots;
	}

	public boolean isWipeData() {
		return wipeData;
	}
	
	public String getAndroidSdkHome() {
		return androidSdkHome;
	}
	
    public void setShouldWipeData() {
        wipeData = true;
    }

	public void setAndroidSdkHome(String androidSdkHome) {
		this.androidSdkHome = androidSdkHome;
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
	
	/**
     * Determines whether a snapshot image has already been created for this emulator.
     *
     * @throws IOException If execution of the emulator command fails.
     * @throws InterruptedException If execution of the emulator command is interrupted.
     */
	public boolean hasExistingSnapshot(AndroidSdk androidSdk, StreamTaskListener listener) throws IOException, InterruptedException {
		final PrintStream logger = listener.getLogger();

		// List available snapshots for this emulator
		ByteArrayOutputStream listOutput = new ByteArrayOutputStream();
		String args = String.format("-snapshot-list -no-window -avd %s", getAvdName());
		// Work around
		// https://code.google.com/p/android/issues/detail?id=34233
		// by always using EMULATOR_ARM to view the snapshot list.
		Utils.runAndroidTool(listOutput, logger, androidSdk, com.mead.android.crazymonkey.sdk.Tool.EMULATOR_ARM, args, null);

		// Check whether a Jenkins snapshot was listed in the output
		return Pattern.compile(Constants.REGEX_SNAPSHOT).matcher(listOutput.toString()).find();
	}
	
	/**
     * Gets the command line arguments to pass to "emulator" based on this instance.
     *
     * @return A string of command line arguments.
     */
    public String getCommandArguments(SnapshotState snapshotState, boolean sdkSupportsSnapshots,
            int userPort, int adbPort) {
        StringBuilder sb = new StringBuilder();

        // Set basics
        sb.append("-no-boot-anim");
        sb.append(String.format(" -ports %s,%s", userPort, adbPort));
        
        sb.append(" -avd ");
        sb.append(getAvdName());

        // Snapshots
        if (snapshotState == SnapshotState.BOOT) {
            // For builds after initial snapshot setup, start directly from the "jenkins" snapshot
            sb.append(" -snapshot "+ Constants.SNAPSHOT_NAME);
            sb.append(" -no-snapshot-save");
        } else if (sdkSupportsSnapshots) {
            // For the first boot, or snapshot-free builds, do not load any snapshots that may exist
            sb.append(" -no-snapshot-load");
            sb.append(" -no-snapshot-save");
        }

        // Options
        if (isWipeData()) {
            sb.append(" -wipe-data");
        }
        
        if (!isShowWindow()) {
            sb.append(" -no-window");
        }
        
        if (commandLineOptions != null) {
            sb.append(" ");
            sb.append(commandLineOptions);
        }

        return sb.toString();
    }
    
    public void setExecutable(String executable) {
		this.executable = executable;
	}

	public void setAvdName(String avdName) {
		this.avdName = avdName;
	}

	public void setOsVersion(AndroidPlatform osVersion) {
		this.osVersion = osVersion;
	}

	public void setScreenDensity(ScreenDensity screenDensity) {
		this.screenDensity = screenDensity;
	}

	public void setScreenResolution(ScreenResolution screenResolution) {
		this.screenResolution = screenResolution;
	}

	public void setDeviceLocale(String deviceLocale) {
		this.deviceLocale = deviceLocale;
	}

	public void setTargetAbi(String targetAbi) {
		this.targetAbi = targetAbi;
	}

	public void setSdCardSize(String sdCardSize) {
		this.sdCardSize = sdCardSize;
	}

	public void setAvdNameSuffix(String avdNameSuffix) {
		this.avdNameSuffix = avdNameSuffix;
	}

	public void setHardwareProperties(HardwareProperty[] hardwareProperties) {
		this.hardwareProperties = hardwareProperties;
	}

	public void setWipeData(boolean wipeData) {
		this.wipeData = wipeData;
	}

	public void setShowWindow(boolean showWindow) {
		this.showWindow = showWindow;
	}

	public void setUseSnapshots(boolean useSnapshots) {
		this.useSnapshots = useSnapshots;
	}

	public void setDeleteAfterBuild(boolean deleteAfterBuild) {
		this.deleteAfterBuild = deleteAfterBuild;
	}

	public void setStartupDelay(int startupDelay) {
		this.startupDelay = startupDelay;
	}

	public void setCommandLineOptions(String commandLineOptions) {
		this.commandLineOptions = commandLineOptions;
	}

}
