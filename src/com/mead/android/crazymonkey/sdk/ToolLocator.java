package com.mead.android.crazymonkey.sdk;

public interface ToolLocator {
	String findInSdk(AndroidSdk androidSdk, Tool tool) throws SdkInstallationException;
}
