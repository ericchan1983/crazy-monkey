package com.mead.android.crazymonkey.sdk;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.mead.android.crazymonkey.util.Utils;

public class AndroidSdk {

    /** First version in which snapshots were supported. */
    private static final int SDK_TOOLS_SNAPSHOTS = 9;

    /** First version in which we can automatically install individual SDK components. */
    private static final int SDK_AUTO_INSTALL = 14;

    /** First version in which we can programmatically install system images. */
    private static final int SDK_SYSTEM_IMAGE_INSTALL = 17;

    /** First version that recognises the "sys-img-[arch]-[tag]-[api]" format. */
    private static final int SDK_SYSTEM_IMAGE_NEW_FORMAT = 23;
    
	private final String sdkRoot;
	private final String sdkHome;
	private boolean usesPlatformTools;
	private int sdkToolsVersion;

	public AndroidSdk(String root, String home) throws IOException {
		this.sdkRoot = root;
		this.sdkHome = home;
		if (hasKnownRoot()) {
			determineVersion();
		}
	}

	private void determineVersion() throws IOException {
		// Determine whether SDK has platform tools installed
		File toolsDirectory = new File(sdkRoot, "platform-tools");
		setUsesPlatformTools(toolsDirectory.isDirectory());

		// Determine SDK tools version
		File toolsPropFile = new File(sdkRoot, "tools/source.properties");
		Map<String, String> toolsProperties;
		toolsProperties = Utils.parseConfigFile(toolsPropFile);
		String revisionStr = Utils.fixEmptyAndTrim(toolsProperties.get("Pkg.Revision"));
		if (revisionStr != null) {
			setSdkToolsVersion(Utils.parseRevisionString(revisionStr));
		}
	}

	public boolean isUsesPlatformTools() {
		return usesPlatformTools;
	}

	public void setUsesPlatformTools(boolean usesPlatformTools) {
		this.usesPlatformTools = usesPlatformTools;
	}

	public int getSdkToolsVersion() {
		return sdkToolsVersion;
	}

	public void setSdkToolsVersion(int sdkToolsVersion) {
		this.sdkToolsVersion = sdkToolsVersion;
	}

	public String getSdkRoot() {
		return sdkRoot;
	}

	public String getSdkHome() {
		return sdkHome;
	}

	public boolean hasKnownRoot() {
		return this.sdkRoot != null;
	}
	
	public boolean hasKnownHome() {
        return this.sdkHome != null;
    }
	
	 public boolean supportsSnapshots() {
		return sdkToolsVersion >= SDK_TOOLS_SNAPSHOTS;
	}

	public boolean supportsComponentInstallation() {
		return sdkToolsVersion >= SDK_AUTO_INSTALL;
	}

	public boolean supportsSystemImageInstallation() {
		return sdkToolsVersion >= SDK_SYSTEM_IMAGE_INSTALL;
	}

	public boolean supportsSystemImageNewFormat() {
		return sdkToolsVersion >= SDK_SYSTEM_IMAGE_NEW_FORMAT;
	}

}
