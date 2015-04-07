package com.mead.android.crazymonkey.sdk;


public class DefaultToolLocator implements ToolLocator {
    public String findInSdk(AndroidSdk androidSdk, Tool tool) {
        return "/tools/";
    }
}
