package com.mead.android.crazymonkey.sdk;
import com.mead.android.crazymonkey.AndroidEmulatorException;

public final class SdkInstallationException extends AndroidEmulatorException {

    public SdkInstallationException(String message) {
        super(message);
    }

    SdkInstallationException(String message, Throwable cause) {
        super(message, cause);
    }

    private static final long serialVersionUID = 1L;

}
