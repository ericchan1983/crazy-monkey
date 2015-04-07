// CHECKSTYLE:OFF

package com.mead.android.crazymonkey;

import org.jvnet.localizer.Localizable;
import org.jvnet.localizer.ResourceBundleHolder;

public class Messages {

    private final static ResourceBundleHolder holder = ResourceBundleHolder.get(Messages.class);

    /**
     * Unrecognised Android emulator configuration: ''{0}''
     * 
     */
    public static String EMULATOR_CONFIGURATION_BAD(Object arg1) {
        return holder.format("EMULATOR_CONFIGURATION_BAD", arg1);
    }

    /**
     * Unrecognised Android emulator configuration: ''{0}''
     * 
     */
    public static Localizable _EMULATOR_CONFIGURATION_BAD(Object arg1) {
        return new Localizable(holder, "EMULATOR_CONFIGURATION_BAD", arg1);
    }

    /**
     * Searching for Android projects...
     * 
     */
    public static String FINDING_PROJECTS() {
        return holder.format("FINDING_PROJECTS");
    }

    /**
     * Searching for Android projects...
     * 
     */
    public static Localizable _FINDING_PROJECTS() {
        return new Localizable(holder, "FINDING_PROJECTS");
    }

    /**
     * OS version is required
     * 
     */
    public static String OS_VERSION_REQUIRED() {
        return holder.format("OS_VERSION_REQUIRED");
    }

    /**
     * OS version is required
     * 
     */
    public static Localizable _OS_VERSION_REQUIRED() {
        return new Localizable(holder, "OS_VERSION_REQUIRED");
    }

    /**
     * Install Android project prerequisites
     * 
     */
    public static String INSTALL_PREREQUISITES() {
        return holder.format("INSTALL_PREREQUISITES");
    }

    /**
     * Install Android project prerequisites
     * 
     */
    public static Localizable _INSTALL_PREREQUISITES() {
        return new Localizable(holder, "INSTALL_PREREQUISITES");
    }

    /**
     * Required tools not found (&lt;tt>adb&lt;/tt> etc.)
     * 
     */
    public static String REQUIRED_SDK_TOOLS_NOT_FOUND() {
        return holder.format("REQUIRED_SDK_TOOLS_NOT_FOUND");
    }

    /**
     * Required tools not found (&lt;tt>adb&lt;/tt> etc.)
     * 
     */
    public static Localizable _REQUIRED_SDK_TOOLS_NOT_FOUND() {
        return new Localizable(holder, "REQUIRED_SDK_TOOLS_NOT_FOUND");
    }

    /**
     * Invalid OS version
     * 
     */
    public static String INVALID_OS_VERSION() {
        return holder.format("INVALID_OS_VERSION");
    }

    /**
     * Invalid OS version
     * 
     */
    public static Localizable _INVALID_OS_VERSION() {
        return new Localizable(holder, "INVALID_OS_VERSION");
    }

    /**
     * AVD name is required
     * 
     */
    public static String AVD_NAME_REQUIRED() {
        return holder.format("AVD_NAME_REQUIRED");
    }

    /**
     * AVD name is required
     * 
     */
    public static Localizable _AVD_NAME_REQUIRED() {
        return new Localizable(holder, "AVD_NAME_REQUIRED");
    }

    /**
     * Starting Android emulator
     * 
     */
    public static String STARTING_EMULATOR() {
        return holder.format("STARTING_EMULATOR");
    }

    /**
     * Starting Android emulator
     * 
     */
    public static Localizable _STARTING_EMULATOR() {
        return new Localizable(holder, "STARTING_EMULATOR");
    }

    /**
     * The desired platform ''{0}'' requires that you install a system image in order to create an AVD.
     * Use the Android SDK Manager to install the ''ARM EABI v7a System Image'' for this platform.
     * 
     */
    public static String ABI_REQUIRED(Object arg1) {
        return holder.format("ABI_REQUIRED", arg1);
    }

    /**
     * The desired platform ''{0}'' requires that you install a system image in order to create an AVD.
     * Use the Android SDK Manager to install the ''ARM EABI v7a System Image'' for this platform.
     * 
     */
    public static Localizable _ABI_REQUIRED(Object arg1) {
        return new Localizable(holder, "ABI_REQUIRED", arg1);
    }

    /**
     * Resolution should be xVGA, or WWWxHHH
     * 
     */
    public static String INVALID_RESOLUTION_FORMAT() {
        return holder.format("INVALID_RESOLUTION_FORMAT");
    }

    /**
     * Resolution should be xVGA, or WWWxHHH
     * 
     */
    public static Localizable _INVALID_RESOLUTION_FORMAT() {
        return new Localizable(holder, "INVALID_RESOLUTION_FORMAT");
    }

    /**
     * Save an Android emulator snapshot
     * 
     */
    public static String SAVE_EMULATOR_SNAPSHOT() {
        return holder.format("SAVE_EMULATOR_SNAPSHOT");
    }

    /**
     * Save an Android emulator snapshot
     * 
     */
    public static Localizable _SAVE_EMULATOR_SNAPSHOT() {
        return new Localizable(holder, "SAVE_EMULATOR_SNAPSHOT");
    }

    /**
     * No Android SDK found; let''s install it automatically...
     * 
     */
    public static String INSTALLING_SDK() {
        return holder.format("INSTALLING_SDK");
    }

    /**
     * No Android SDK found; let''s install it automatically...
     * 
     */
    public static Localizable _INSTALLING_SDK() {
        return new Localizable(holder, "INSTALLING_SDK");
    }

    /**
     * Uninstall Android package
     * 
     */
    public static String UNINSTALL_ANDROID_PACKAGE() {
        return holder.format("UNINSTALL_ANDROID_PACKAGE");
    }

    /**
     * Uninstall Android package
     * 
     */
    public static Localizable _UNINSTALL_ANDROID_PACKAGE() {
        return new Localizable(holder, "UNINSTALL_ANDROID_PACKAGE");
    }

    /**
     * Archiving emulator log
     * 
     */
    public static String ARCHIVING_LOG() {
        return holder.format("ARCHIVING_LOG");
    }

    /**
     * Archiving emulator log
     * 
     */
    public static Localizable _ARCHIVING_LOG() {
        return new Localizable(holder, "ARCHIVING_LOG");
    }

    /**
     * Failed to delete AVD: {0}
     * 
     */
    public static String FAILED_TO_DELETE_AVD(Object arg1) {
        return holder.format("FAILED_TO_DELETE_AVD", arg1);
    }

    /**
     * Failed to delete AVD: {0}
     * 
     */
    public static Localizable _FAILED_TO_DELETE_AVD(Object arg1) {
        return new Localizable(holder, "FAILED_TO_DELETE_AVD", arg1);
    }

    /**
     * Failed to shut down emulator; the process may still be running...
     * 
     */
    public static String EMULATOR_SHUTDOWN_FAILED() {
        return holder.format("EMULATOR_SHUTDOWN_FAILED");
    }

    /**
     * Failed to shut down emulator; the process may still be running...
     * 
     */
    public static Localizable _EMULATOR_SHUTDOWN_FAILED() {
        return new Localizable(holder, "EMULATOR_SHUTDOWN_FAILED");
    }

    /**
     * Screen density should be a number
     * 
     */
    public static String SCREEN_DENSITY_NOT_NUMERIC() {
        return holder.format("SCREEN_DENSITY_NOT_NUMERIC");
    }

    /**
     * Screen density should be a number
     * 
     */
    public static Localizable _SCREEN_DENSITY_NOT_NUMERIC() {
        return new Localizable(holder, "SCREEN_DENSITY_NOT_NUMERIC");
    }

    /**
     * Downloading and installing Android SDK from {0}
     * 
     */
    public static String DOWNLOADING_SDK_FROM(Object arg1) {
        return holder.format("DOWNLOADING_SDK_FROM", arg1);
    }

    /**
     * Downloading and installing Android SDK from {0}
     * 
     */
    public static Localizable _DOWNLOADING_SDK_FROM(Object arg1) {
        return new Localizable(holder, "DOWNLOADING_SDK_FROM", arg1);
    }

    /**
     * Unrecognised target ABI
     * 
     */
    public static String INVALID_TARGET_ABI() {
        return holder.format("INVALID_TARGET_ABI");
    }

    /**
     * Unrecognised target ABI
     * 
     */
    public static Localizable _INVALID_TARGET_ABI() {
        return new Localizable(holder, "INVALID_TARGET_ABI");
    }

    /**
     * Could not start AVD ''{0}'', as it could not be found at ''{1}''
     * 
     */
    public static String AVD_DOES_NOT_EXIST(Object arg1, Object arg2) {
        return holder.format("AVD_DOES_NOT_EXIST", arg1, arg2);
    }

    /**
     * Could not start AVD ''{0}'', as it could not be found at ''{1}''
     * 
     */
    public static Localizable _AVD_DOES_NOT_EXIST(Object arg1, Object arg2) {
        return new Localizable(holder, "AVD_DOES_NOT_EXIST", arg1, arg2);
    }

    /**
     * No Android projects found; won't install any dependencies.
     * 
     */
    public static String NO_PROJECTS_FOUND_FOR_PREREQUISITES() {
        return holder.format("NO_PROJECTS_FOUND_FOR_PREREQUISITES");
    }

    /**
     * No Android projects found; won't install any dependencies.
     * 
     */
    public static Localizable _NO_PROJECTS_FOUND_FOR_PREREQUISITES() {
        return new Localizable(holder, "NO_PROJECTS_FOUND_FOR_PREREQUISITES");
    }

    /**
     * Interrupted while waiting for emulator to finish booting.
     * 
     */
    public static String INTERRUPTED_DURING_BOOT_COMPLETION() {
        return holder.format("INTERRUPTED_DURING_BOOT_COMPLETION");
    }

    /**
     * Interrupted while waiting for emulator to finish booting.
     * 
     */
    public static Localizable _INTERRUPTED_DURING_BOOT_COMPLETION() {
        return new Localizable(holder, "INTERRUPTED_DURING_BOOT_COMPLETION");
    }

    /**
     * It appears that the configured platform is based on Android 4.0 or newer.
     * This requires the ''ARM EABI v7a System Image'' package which cannot be automatically installed with SDK Tools r16 or earlier.
     * Please install this component manually via the Android SDK Manager, or upgrade to SDK Tools r17
     * 
     */
    public static String ABI_INSTALLATION_UNSUPPORTED() {
        return holder.format("ABI_INSTALLATION_UNSUPPORTED");
    }

    /**
     * It appears that the configured platform is based on Android 4.0 or newer.
     * This requires the ''ARM EABI v7a System Image'' package which cannot be automatically installed with SDK Tools r16 or earlier.
     * Please install this component manually via the Android SDK Manager, or upgrade to SDK Tools r17
     * 
     */
    public static Localizable _ABI_INSTALLATION_UNSUPPORTED() {
        return new Localizable(holder, "ABI_INSTALLATION_UNSUPPORTED");
    }

    /**
     * Unstable
     * 
     */
    public static String BUILD_RESULT_UNSTABLE() {
        return holder.format("BUILD_RESULT_UNSTABLE");
    }

    /**
     * Unstable
     * 
     */
    public static Localizable _BUILD_RESULT_UNSTABLE() {
        return new Localizable(holder, "BUILD_RESULT_UNSTABLE");
    }

    /**
     * Creating build files for {0} project at ''{1}''...
     * 
     */
    public static String CREATING_BUILD_FILES(Object arg1, Object arg2) {
        return holder.format("CREATING_BUILD_FILES", arg1, arg2);
    }

    /**
     * Creating build files for {0} project at ''{1}''...
     * 
     */
    public static Localizable _CREATING_BUILD_FILES(Object arg1, Object arg2) {
        return new Localizable(holder, "CREATING_BUILD_FILES", arg1, arg2);
    }

    /**
     * No APK file was specified to be installed
     * 
     */
    public static String APK_NOT_SPECIFIED() {
        return holder.format("APK_NOT_SPECIFIED");
    }

    /**
     * No APK file was specified to be installed
     * 
     */
    public static Localizable _APK_NOT_SPECIFIED() {
        return new Localizable(holder, "APK_NOT_SPECIFIED");
    }

    /**
     * Does not look like an Android SDK directory
     * 
     */
    public static String INVALID_SDK_DIRECTORY() {
        return holder.format("INVALID_SDK_DIRECTORY");
    }

    /**
     * Does not look like an Android SDK directory
     * 
     */
    public static Localizable _INVALID_SDK_DIRECTORY() {
        return new Localizable(holder, "INVALID_SDK_DIRECTORY");
    }

    /**
     * Could not determine package name from APK file ''{0}''; cannot uninstall
     * 
     */
    public static String COULD_NOT_DETERMINE_APK_PACKAGE(Object arg1) {
        return holder.format("COULD_NOT_DETERMINE_APK_PACKAGE", arg1);
    }

    /**
     * Could not determine package name from APK file ''{0}''; cannot uninstall
     * 
     */
    public static Localizable _COULD_NOT_DETERMINE_APK_PACKAGE(Object arg1) {
        return new Localizable(holder, "COULD_NOT_DETERMINE_APK_PACKAGE", arg1);
    }

    /**
     * Loading snapshot ''{0}'' into emulator on port {1}...
     * 
     */
    public static String LOADING_SNAPSHOT(Object arg1, Object arg2) {
        return holder.format("LOADING_SNAPSHOT", arg1, arg2);
    }

    /**
     * Loading snapshot ''{0}'' into emulator on port {1}...
     * 
     */
    public static Localizable _LOADING_SNAPSHOT(Object arg1, Object arg2) {
        return new Localizable(holder, "LOADING_SNAPSHOT", arg1, arg2);
    }

    /**
     * Found {0} projects to create build files for.
     * 
     */
    public static String FOUND_PROJECTS_TO_UPDATE(Object arg1) {
        return holder.format("FOUND_PROJECTS_TO_UPDATE", arg1);
    }

    /**
     * Found {0} projects to create build files for.
     * 
     */
    public static Localizable _FOUND_PROJECTS_TO_UPDATE(Object arg1) {
        return new Localizable(holder, "FOUND_PROJECTS_TO_UPDATE", arg1);
    }

    /**
     * [none found; relying on PATH]
     * 
     */
    public static String USING_PATH() {
        return holder.format("USING_PATH");
    }

    /**
     * [none found; relying on PATH]
     * 
     */
    public static Localizable _USING_PATH() {
        return new Localizable(holder, "USING_PATH");
    }

    /**
     * Load an Android emulator snapshot
     * 
     */
    public static String LOAD_EMULATOR_SNAPSHOT() {
        return holder.format("LOAD_EMULATOR_SNAPSHOT");
    }

    /**
     * Load an Android emulator snapshot
     * 
     */
    public static Localizable _LOAD_EMULATOR_SNAPSHOT() {
        return new Localizable(holder, "LOAD_EMULATOR_SNAPSHOT");
    }

    /**
     * Install Android package
     * 
     */
    public static String INSTALL_ANDROID_PACKAGE() {
        return holder.format("INSTALL_ANDROID_PACKAGE");
    }

    /**
     * Install Android package
     * 
     */
    public static Localizable _INSTALL_ANDROID_PACKAGE() {
        return new Localizable(holder, "INSTALL_ANDROID_PACKAGE");
    }

    /**
     * Emulator is ready for use (took {0} seconds)
     * 
     */
    public static String EMULATOR_IS_READY(Object arg1) {
        return holder.format("EMULATOR_IS_READY", arg1);
    }

    /**
     * Emulator is ready for use (took {0} seconds)
     * 
     */
    public static Localizable _EMULATOR_IS_READY(Object arg1) {
        return new Localizable(holder, "EMULATOR_IS_READY", arg1);
    }

    /**
     * Could not check for boot completion:
     * 
     */
    public static String COULD_NOT_CHECK_BOOT_COMPLETION() {
        return holder.format("COULD_NOT_CHECK_BOOT_COMPLETION");
    }

    /**
     * Could not check for boot completion:
     * 
     */
    public static Localizable _COULD_NOT_CHECK_BOOT_COMPLETION() {
        return new Localizable(holder, "COULD_NOT_CHECK_BOOT_COMPLETION");
    }

    /**
     * Could not create Android emulator: {0}
     * 
     */
    public static String COULD_NOT_CREATE_EMULATOR(Object arg1) {
        return holder.format("COULD_NOT_CREATE_EMULATOR", arg1);
    }

    /**
     * Could not create Android emulator: {0}
     * 
     */
    public static Localizable _COULD_NOT_CREATE_EMULATOR(Object arg1) {
        return new Localizable(holder, "COULD_NOT_CREATE_EMULATOR", arg1);
    }

    /**
     * Saving snapshot ''{0}'' for emulator on port {1}...
     * 
     */
    public static String SAVING_SNAPSHOT(Object arg1, Object arg2) {
        return holder.format("SAVING_SNAPSHOT", arg1, arg2);
    }

    /**
     * Saving snapshot ''{0}'' for emulator on port {1}...
     * 
     */
    public static Localizable _SAVING_SNAPSHOT(Object arg1, Object arg2) {
        return new Localizable(holder, "SAVING_SNAPSHOT", arg1, arg2);
    }

    /**
     * Emulator did not appear to start; giving up
     * 
     */
    public static String EMULATOR_DID_NOT_START() {
        return holder.format("EMULATOR_DID_NOT_START");
    }

    /**
     * Emulator did not appear to start; giving up
     * 
     */
    public static Localizable _EMULATOR_DID_NOT_START() {
        return new Localizable(holder, "EMULATOR_DID_NOT_START");
    }

    /**
     * Android SDK directory needs to be specified in order to create an emulator
     * 
     */
    public static String SDK_NOT_SPECIFIED() {
        return holder.format("SDK_NOT_SPECIFIED");
    }

    /**
     * Android SDK directory needs to be specified in order to create an emulator
     * 
     */
    public static Localizable _SDK_NOT_SPECIFIED() {
        return new Localizable(holder, "SDK_NOT_SPECIFIED");
    }

    /**
     * Failed to read manifest file at ''{0}''.
     * 
     */
    public static String FAILED_TO_READ_MANIFEST(Object arg1) {
        return holder.format("FAILED_TO_READ_MANIFEST", arg1);
    }

    /**
     * Failed to read manifest file at ''{0}''.
     * 
     */
    public static Localizable _FAILED_TO_READ_MANIFEST(Object arg1) {
        return new Localizable(holder, "FAILED_TO_READ_MANIFEST", arg1);
    }

    /**
     * Stopped responding after {0} of {1} events
     * 
     */
    public static String MONKEY_RESULT_ANR(Object arg1, Object arg2) {
        return holder.format("MONKEY_RESULT_ANR", arg1, arg2);
    }

    /**
     * Stopped responding after {0} of {1} events
     * 
     */
    public static Localizable _MONKEY_RESULT_ANR(Object arg1, Object arg2) {
        return new Localizable(holder, "MONKEY_RESULT_ANR", arg1, arg2);
    }

    /**
     * Uninstalling APK with package ID ''{0}''
     * 
     */
    public static String UNINSTALLING_APK(Object arg1) {
        return holder.format("UNINSTALLING_APK", arg1);
    }

    /**
     * Uninstalling APK with package ID ''{0}''
     * 
     */
    public static Localizable _UNINSTALLING_APK(Object arg1) {
        return new Localizable(holder, "UNINSTALLING_APK", arg1);
    }

    /**
     * Failed to determine type of project at ''{0}''.
     * 
     */
    public static String FAILED_TO_DETERMINE_PROJECT_TYPE(Object arg1) {
        return holder.format("FAILED_TO_DETERMINE_PROJECT_TYPE", arg1);
    }

    /**
     * Failed to determine type of project at ''{0}''.
     * 
     */
    public static Localizable _FAILED_TO_DETERMINE_PROJECT_TYPE(Object arg1) {
        return new Localizable(holder, "FAILED_TO_DETERMINE_PROJECT_TYPE", arg1);
    }

    /**
     * Could not find APK file ''{0}'' to be installed
     * 
     */
    public static String APK_NOT_FOUND(Object arg1) {
        return holder.format("APK_NOT_FOUND", arg1);
    }

    /**
     * Could not find APK file ''{0}'' to be installed
     * 
     */
    public static Localizable _APK_NOT_FOUND(Object arg1) {
        return new Localizable(holder, "APK_NOT_FOUND", arg1);
    }

    /**
     * Creating snapshot...
     * 
     */
    public static String EMULATOR_PAUSED_SNAPSHOT() {
        return holder.format("EMULATOR_PAUSED_SNAPSHOT");
    }

    /**
     * Creating snapshot...
     * 
     */
    public static Localizable _EMULATOR_PAUSED_SNAPSHOT() {
        return new Localizable(holder, "EMULATOR_PAUSED_SNAPSHOT");
    }

    /**
     * Creating snapshot...
     * 
     */
    public static String LOG_CREATING_SNAPSHOT() {
        return holder.format("LOG_CREATING_SNAPSHOT");
    }

    /**
     * Creating snapshot...
     * 
     */
    public static Localizable _LOG_CREATING_SNAPSHOT() {
        return new Localizable(holder, "LOG_CREATING_SNAPSHOT");
    }

    /**
     * However, this cannot be automatically installed as SDK Tools r14 or newer is required...
     * 
     */
    public static String SDK_COMPONENT_INSTALLATION_UNSUPPORTED() {
        return holder.format("SDK_COMPONENT_INSTALLATION_UNSUPPORTED");
    }

    /**
     * However, this cannot be automatically installed as SDK Tools r14 or newer is required...
     * 
     */
    public static Localizable _SDK_COMPONENT_INSTALLATION_UNSUPPORTED() {
        return new Localizable(holder, "SDK_COMPONENT_INSTALLATION_UNSUPPORTED");
    }

    /**
     * Could not open monkey output file ''{0}''
     * 
     */
    public static String NO_MONKEY_OUTPUT(Object arg1) {
        return holder.format("NO_MONKEY_OUTPUT", arg1);
    }

    /**
     * Could not open monkey output file ''{0}''
     * 
     */
    public static Localizable _NO_MONKEY_OUTPUT(Object arg1) {
        return new Localizable(holder, "NO_MONKEY_OUTPUT", arg1);
    }

    /**
     * Aborting emulator command ''{0}'' as it''s taking too long...
     * 
     */
    public static String SENDING_COMMAND_TIMED_OUT(Object arg1) {
        return holder.format("SENDING_COMMAND_TIMED_OUT", arg1);
    }

    /**
     * Aborting emulator command ''{0}'' as it''s taking too long...
     * 
     */
    public static Localizable _SENDING_COMMAND_TIMED_OUT(Object arg1) {
        return new Localizable(holder, "SENDING_COMMAND_TIMED_OUT", arg1);
    }

    /**
     * Cannot find desired platform ''{0}''; are you sure it is installed?
     * 
     */
    public static String PLATFORM_NOT_FOUND(Object arg1) {
        return holder.format("PLATFORM_NOT_FOUND", arg1);
    }

    /**
     * Cannot find desired platform ''{0}''; are you sure it is installed?
     * 
     */
    public static Localizable _PLATFORM_NOT_FOUND(Object arg1) {
        return new Localizable(holder, "PLATFORM_NOT_FOUND", arg1);
    }

    /**
     * Cannot start Android emulator due to misconfiguration: {0}
     * 
     */
    public static String ERROR_MISCONFIGURED(Object arg1) {
        return holder.format("ERROR_MISCONFIGURED", arg1);
    }

    /**
     * Cannot start Android emulator due to misconfiguration: {0}
     * 
     */
    public static Localizable _ERROR_MISCONFIGURED(Object arg1) {
        return new Localizable(holder, "ERROR_MISCONFIGURED", arg1);
    }

    /**
     * Giving the system some time to settle before creating initial snapshot...
     * 
     */
    public static String WAITING_INITIAL_SNAPSHOT() {
        return holder.format("WAITING_INITIAL_SNAPSHOT");
    }

    /**
     * Giving the system some time to settle before creating initial snapshot...
     * 
     */
    public static Localizable _WAITING_INITIAL_SNAPSHOT() {
        return new Localizable(holder, "WAITING_INITIAL_SNAPSHOT");
    }

    /**
     * Does not appear to be a valid directory
     * 
     */
    public static String INVALID_DIRECTORY() {
        return holder.format("INVALID_DIRECTORY");
    }

    /**
     * Does not appear to be a valid directory
     * 
     */
    public static Localizable _INVALID_DIRECTORY() {
        return new Localizable(holder, "INVALID_DIRECTORY");
    }

    /**
     * Could not create directory ''{0}''
     * 
     */
    public static String AVD_DIRECTORY_CREATION_FAILED(Object arg1) {
        return holder.format("AVD_DIRECTORY_CREATION_FAILED", arg1);
    }

    /**
     * Could not create directory ''{0}''
     * 
     */
    public static Localizable _AVD_DIRECTORY_CREATION_FAILED(Object arg1) {
        return new Localizable(holder, "AVD_DIRECTORY_CREATION_FAILED", arg1);
    }

    /**
     * No package ID was specified to be uninstalled
     * 
     */
    public static String PACKAGE_ID_NOT_SPECIFIED() {
        return holder.format("PACKAGE_ID_NOT_SPECIFIED");
    }

    /**
     * No package ID was specified to be uninstalled
     * 
     */
    public static Localizable _PACKAGE_ID_NOT_SPECIFIED() {
        return new Localizable(holder, "PACKAGE_ID_NOT_SPECIFIED");
    }

    /**
     * Snapshot creation failed; will try again during the next build
     * 
     */
    public static String SNAPSHOT_CREATION_FAILED() {
        return holder.format("SNAPSHOT_CREATION_FAILED");
    }

    /**
     * Snapshot creation failed; will try again during the next build
     * 
     */
    public static Localizable _SNAPSHOT_CREATION_FAILED() {
        return new Localizable(holder, "SNAPSHOT_CREATION_FAILED");
    }

    /**
     * Emulator could not be started as AVD ''{0}'' is already running on this machine
     * 
     */
    public static String EMULATOR_ALREADY_IN_USE(Object arg1) {
        return holder.format("EMULATOR_ALREADY_IN_USE", arg1);
    }

    /**
     * Emulator could not be started as AVD ''{0}'' is already running on this machine
     * 
     */
    public static Localizable _EMULATOR_ALREADY_IN_USE(Object arg1) {
        return new Localizable(holder, "EMULATOR_ALREADY_IN_USE", arg1);
    }

    /**
     * Interrupted while creating new emulator
     * 
     */
    public static String AVD_CREATION_INTERRUPTED() {
        return holder.format("AVD_CREATION_INTERRUPTED");
    }

    /**
     * Interrupted while creating new emulator
     * 
     */
    public static Localizable _AVD_CREATION_INTERRUPTED() {
        return new Localizable(holder, "AVD_CREATION_INTERRUPTED");
    }

    /**
     * Failed to download Android SDK
     * 
     */
    public static String SDK_DOWNLOAD_FAILED() {
        return holder.format("SDK_DOWNLOAD_FAILED");
    }

    /**
     * Failed to download Android SDK
     * 
     */
    public static Localizable _SDK_DOWNLOAD_FAILED() {
        return new Localizable(holder, "SDK_DOWNLOAD_FAILED");
    }

    /**
     * Could not find AVD directory ''{0}''
     * 
     */
    public static String AVD_DIRECTORY_NOT_FOUND(Object arg1) {
        return holder.format("AVD_DIRECTORY_NOT_FOUND", arg1);
    }

    /**
     * Could not find AVD directory ''{0}''
     * 
     */
    public static Localizable _AVD_DIRECTORY_NOT_FOUND(Object arg1) {
        return new Localizable(holder, "AVD_DIRECTORY_NOT_FOUND", arg1);
    }

    /**
     * Emulator was shut down before it finished booting
     * 
     */
    public static String EMULATOR_STOPPED_DURING_BOOT() {
        return holder.format("EMULATOR_STOPPED_DURING_BOOT");
    }

    /**
     * Emulator was shut down before it finished booting
     * 
     */
    public static Localizable _EMULATOR_STOPPED_DURING_BOOT() {
        return new Localizable(holder, "EMULATOR_STOPPED_DURING_BOOT");
    }

    /**
     * AVD creation command failed to complete normally
     * 
     */
    public static String AVD_CREATION_ABORTED() {
        return holder.format("AVD_CREATION_ABORTED");
    }

    /**
     * AVD creation command failed to complete normally
     * 
     */
    public static Localizable _AVD_CREATION_ABORTED() {
        return new Localizable(holder, "AVD_CREATION_ABORTED");
    }

    /**
     * Creating Android AVD: {0}
     * 
     */
    public static String CREATING_AVD(Object arg1) {
        return holder.format("CREATING_AVD", arg1);
    }

    /**
     * Creating Android AVD: {0}
     * 
     */
    public static Localizable _CREATING_AVD(Object arg1) {
        return new Localizable(holder, "CREATING_AVD", arg1);
    }

    /**
     * Starting Android emulator from snapshot
     * 
     */
    public static String STARTING_EMULATOR_FROM_SNAPSHOT() {
        return holder.format("STARTING_EMULATOR_FROM_SNAPSHOT");
    }

    /**
     * Starting Android emulator from snapshot
     * 
     */
    public static Localizable _STARTING_EMULATOR_FROM_SNAPSHOT() {
        return new Localizable(holder, "STARTING_EMULATOR_FROM_SNAPSHOT");
    }

    /**
     * That doesn''t look right for Android {0}. Did you mean WXGA720 or WXGA800?
     * 
     */
    public static String SUSPECT_RESOLUTION_ANDROID_4(Object arg1) {
        return holder.format("SUSPECT_RESOLUTION_ANDROID_4", arg1);
    }

    /**
     * That doesn''t look right for Android {0}. Did you mean WXGA720 or WXGA800?
     * 
     */
    public static Localizable _SUSPECT_RESOLUTION_ANDROID_4(Object arg1) {
        return new Localizable(holder, "SUSPECT_RESOLUTION_ANDROID_4", arg1);
    }

    /**
     * That doesn''t look right for Android {0}. Did you mean WXGA?
     * 
     */
    public static String SUSPECT_RESOLUTION_ANDROID_3(Object arg1) {
        return holder.format("SUSPECT_RESOLUTION_ANDROID_3", arg1);
    }

    /**
     * That doesn''t look right for Android {0}. Did you mean WXGA?
     * 
     */
    public static Localizable _SUSPECT_RESOLUTION_ANDROID_3(Object arg1) {
        return new Localizable(holder, "SUSPECT_RESOLUTION_ANDROID_3", arg1);
    }

    /**
     * Could not add SD card to emulator:
     * 
     */
    public static String SD_CARD_CREATION_FAILED() {
        return holder.format("SD_CARD_CREATION_FAILED");
    }

    /**
     * Could not add SD card to emulator:
     * 
     */
    public static Localizable _SD_CARD_CREATION_FAILED() {
        return new Localizable(holder, "SD_CARD_CREATION_FAILED");
    }

    /**
     * Failed to execute emulator command ''{0}'': {1}
     * 
     */
    public static String SENDING_COMMAND_FAILED(Object arg1, Object arg2) {
        return holder.format("SENDING_COMMAND_FAILED", arg1, arg2);
    }

    /**
     * Failed to execute emulator command ''{0}'': {1}
     * 
     */
    public static Localizable _SENDING_COMMAND_FAILED(Object arg1, Object arg2) {
        return new Localizable(holder, "SENDING_COMMAND_FAILED", arg1, arg2);
    }

    /**
     * SD card size must be at least 9 megabytes
     * 
     */
    public static String SD_CARD_SIZE_TOO_SMALL() {
        return holder.format("SD_CARD_SIZE_TOO_SMALL");
    }

    /**
     * SD card size must be at least 9 megabytes
     * 
     */
    public static Localizable _SD_CARD_SIZE_TOO_SMALL() {
        return new Localizable(holder, "SD_CARD_SIZE_TOO_SMALL");
    }

    /**
     * Failed to run AVD creation command
     * 
     */
    public static String AVD_CREATION_FAILED() {
        return holder.format("AVD_CREATION_FAILED");
    }

    /**
     * Failed to run AVD creation command
     * 
     */
    public static Localizable _AVD_CREATION_FAILED() {
        return new Localizable(holder, "AVD_CREATION_FAILED");
    }

    /**
     * Adding {0} SD card to AVD ''{1}''...
     * 
     */
    public static String ADDING_SD_CARD(Object arg1, Object arg2) {
        return holder.format("ADDING_SD_CARD", arg1, arg2);
    }

    /**
     * Adding {0} SD card to AVD ''{1}''...
     * 
     */
    public static Localizable _ADDING_SD_CARD(Object arg1, Object arg2) {
        return new Localizable(holder, "ADDING_SD_CARD", arg1, arg2);
    }

    /**
     * Screen resolution is required
     * 
     */
    public static String SCREEN_RESOLUTION_REQUIRED() {
        return holder.format("SCREEN_RESOLUTION_REQUIRED");
    }

    /**
     * Screen resolution is required
     * 
     */
    public static Localizable _SCREEN_RESOLUTION_REQUIRED() {
        return new Localizable(holder, "SCREEN_RESOLUTION_REQUIRED");
    }

    /**
     * Could not connect to running emulator; cannot continue
     * 
     */
    public static String CANNOT_CONNECT_TO_EMULATOR() {
        return holder.format("CANNOT_CONNECT_TO_EMULATOR");
    }

    /**
     * Could not connect to running emulator; cannot continue
     * 
     */
    public static Localizable _CANNOT_CONNECT_TO_EMULATOR() {
        return new Localizable(holder, "CANNOT_CONNECT_TO_EMULATOR");
    }

    /**
     * Timed-out after waiting {0} seconds for emulator
     * 
     */
    public static String BOOT_COMPLETION_TIMED_OUT(Object arg1) {
        return holder.format("BOOT_COMPLETION_TIMED_OUT", arg1);
    }

    /**
     * Timed-out after waiting {0} seconds for emulator
     * 
     */
    public static Localizable _BOOT_COMPLETION_TIMED_OUT(Object arg1) {
        return new Localizable(holder, "BOOT_COMPLETION_TIMED_OUT", arg1);
    }

    /**
     * Reading platform from project file failed.
     * 
     */
    public static String READING_PROJECT_FILE_FAILED() {
        return holder.format("READING_PROJECT_FILE_FAILED");
    }

    /**
     * Reading platform from project file failed.
     * 
     */
    public static Localizable _READING_PROJECT_FILE_FAILED() {
        return new Localizable(holder, "READING_PROJECT_FILE_FAILED");
    }

    /**
     * library
     * 
     */
    public static String PROJECT_TYPE_LIBRARY() {
        return holder.format("PROJECT_TYPE_LIBRARY");
    }

    /**
     * library
     * 
     */
    public static Localizable _PROJECT_TYPE_LIBRARY() {
        return new Localizable(holder, "PROJECT_TYPE_LIBRARY");
    }

    /**
     * Attempting to unlock emulator screen
     * 
     */
    public static String UNLOCKING_SCREEN() {
        return holder.format("UNLOCKING_SCREEN");
    }

    /**
     * Attempting to unlock emulator screen
     * 
     */
    public static Localizable _UNLOCKING_SCREEN() {
        return new Localizable(holder, "UNLOCKING_SCREEN");
    }

    /**
     * Succeeded after {0} events
     * 
     */
    public static String MONKEY_RESULT_SUCCESS(Object arg1) {
        return holder.format("MONKEY_RESULT_SUCCESS", arg1);
    }

    /**
     * Succeeded after {0} events
     * 
     */
    public static Localizable _MONKEY_RESULT_SUCCESS(Object arg1) {
        return new Localizable(holder, "MONKEY_RESULT_SUCCESS", arg1);
    }

    /**
     * test
     * 
     */
    public static String PROJECT_TYPE_TEST() {
        return holder.format("PROJECT_TYPE_TEST");
    }

    /**
     * test
     * 
     */
    public static Localizable _PROJECT_TYPE_TEST() {
        return new Localizable(holder, "PROJECT_TYPE_TEST");
    }

    /**
     * No monkey output was provided
     * 
     */
    public static String MONKEY_RESULT_NONE() {
        return holder.format("MONKEY_RESULT_NONE");
    }

    /**
     * No monkey output was provided
     * 
     */
    public static Localizable _MONKEY_RESULT_NONE() {
        return new Localizable(holder, "MONKEY_RESULT_NONE");
    }

    /**
     * Ensuring platform(s) are installed: {0}
     * 
     */
    public static String ENSURING_PLATFORMS_INSTALLED(Object arg1) {
        return holder.format("ENSURING_PLATFORMS_INSTALLED", arg1);
    }

    /**
     * Ensuring platform(s) are installed: {0}
     * 
     */
    public static Localizable _ENSURING_PLATFORMS_INSTALLED(Object arg1) {
        return new Localizable(holder, "ENSURING_PLATFORMS_INSTALLED", arg1);
    }

    /**
     * Android monkey test: {0}
     * 
     */
    public static String MONKEY_RESULT(Object arg1) {
        return holder.format("MONKEY_RESULT", arg1);
    }

    /**
     * Android monkey test: {0}
     * 
     */
    public static Localizable _MONKEY_RESULT(Object arg1) {
        return new Localizable(holder, "MONKEY_RESULT", arg1);
    }

    /**
     * Run an Android emulator during build
     * 
     */
    public static String JOB_DESCRIPTION() {
        return holder.format("JOB_DESCRIPTION");
    }

    /**
     * Run an Android emulator during build
     * 
     */
    public static Localizable _JOB_DESCRIPTION() {
        return new Localizable(holder, "JOB_DESCRIPTION");
    }

    /**
     * Do nothing
     * 
     */
    public static String BUILD_RESULT_IGNORE() {
        return holder.format("BUILD_RESULT_IGNORE");
    }

    /**
     * Do nothing
     * 
     */
    public static Localizable _BUILD_RESULT_IGNORE() {
        return new Localizable(holder, "BUILD_RESULT_IGNORE");
    }

    /**
     * Could not determine results from file
     * 
     */
    public static String MONKEY_RESULT_UNRECOGNISED() {
        return holder.format("MONKEY_RESULT_UNRECOGNISED");
    }

    /**
     * Could not determine results from file
     * 
     */
    public static Localizable _MONKEY_RESULT_UNRECOGNISED() {
        return new Localizable(holder, "MONKEY_RESULT_UNRECOGNISED");
    }

    /**
     * Create Android build files
     * 
     */
    public static String CREATE_PROJECT_BUILD_FILES() {
        return holder.format("CREATE_PROJECT_BUILD_FILES");
    }

    /**
     * Create Android build files
     * 
     */
    public static Localizable _CREATE_PROJECT_BUILD_FILES() {
        return new Localizable(holder, "CREATE_PROJECT_BUILD_FILES");
    }

    /**
     * Base SDK installed successfully
     * 
     */
    public static String BASE_SDK_INSTALLED() {
        return holder.format("BASE_SDK_INSTALLED");
    }

    /**
     * Base SDK installed successfully
     * 
     */
    public static Localizable _BASE_SDK_INSTALLED() {
        return new Localizable(holder, "BASE_SDK_INSTALLED");
    }

    /**
     * Detected failures in monkey output, but won''t change the build result
     * 
     */
    public static String MONKEY_IGNORING_RESULT() {
        return holder.format("MONKEY_IGNORING_RESULT");
    }

    /**
     * Detected failures in monkey output, but won''t change the build result
     * 
     */
    public static Localizable _MONKEY_IGNORING_RESULT() {
        return new Localizable(holder, "MONKEY_IGNORING_RESULT");
    }

    /**
     * No Android projects found to create build files for.
     * 
     */
    public static String NO_PROJECTS_FOUND_TO_UPDATE() {
        return holder.format("NO_PROJECTS_FOUND_TO_UPDATE");
    }

    /**
     * No Android projects found to create build files for.
     * 
     */
    public static Localizable _NO_PROJECTS_FOUND_TO_UPDATE() {
        return new Localizable(holder, "NO_PROJECTS_FOUND_TO_UPDATE");
    }

    /**
     * SD card size should be numeric with suffix, e.g. 32M
     * 
     */
    public static String INVALID_SD_CARD_SIZE() {
        return holder.format("INVALID_SD_CARD_SIZE");
    }

    /**
     * SD card size should be numeric with suffix, e.g. 32M
     * 
     */
    public static Localizable _INVALID_SD_CARD_SIZE() {
        return new Localizable(holder, "INVALID_SD_CARD_SIZE");
    }

    /**
     * Installing APK file ''{0}''
     * 
     */
    public static String INSTALLING_APK(Object arg1) {
        return holder.format("INSTALLING_APK", arg1);
    }

    /**
     * Installing APK file ''{0}''
     * 
     */
    public static Localizable _INSTALLING_APK(Object arg1) {
        return new Localizable(holder, "INSTALLING_APK", arg1);
    }

    /**
     * Locale will default to ''{0}'' if not specified
     * 
     */
    public static String DEFAULT_LOCALE_WARNING(Object arg1) {
        return holder.format("DEFAULT_LOCALE_WARNING", arg1);
    }

    /**
     * Locale will default to ''{0}'' if not specified
     * 
     */
    public static Localizable _DEFAULT_LOCALE_WARNING(Object arg1) {
        return new Localizable(holder, "DEFAULT_LOCALE_WARNING", arg1);
    }

    /**
     * Required Android tools not found in PATH; cannot continue
     * 
     */
    public static String SDK_TOOLS_NOT_FOUND() {
        return holder.format("SDK_TOOLS_NOT_FOUND");
    }

    /**
     * Required Android tools not found in PATH; cannot continue
     * 
     */
    public static Localizable _SDK_TOOLS_NOT_FOUND() {
        return new Localizable(holder, "SDK_TOOLS_NOT_FOUND");
    }

    /**
     * Monkeying around with ''{0}'' (events: {1}, seed: {2})...
     * 
     */
    public static String STARTING_MONKEY(Object arg1, Object arg2, Object arg3) {
        return holder.format("STARTING_MONKEY", arg1, arg2, arg3);
    }

    /**
     * Monkeying around with ''{0}'' (events: {1}, seed: {2})...
     * 
     */
    public static Localizable _STARTING_MONKEY(Object arg1, Object arg2, Object arg3) {
        return new Localizable(holder, "STARTING_MONKEY", arg1, arg2, arg3);
    }

    /**
     * Starting Android emulator and creating initial snapshot
     * 
     */
    public static String STARTING_EMULATOR_SNAPSHOT_INIT() {
        return holder.format("STARTING_EMULATOR_SNAPSHOT_INIT");
    }

    /**
     * Starting Android emulator and creating initial snapshot
     * 
     */
    public static Localizable _STARTING_EMULATOR_SNAPSHOT_INIT() {
        return new Localizable(holder, "STARTING_EMULATOR_SNAPSHOT_INIT");
    }

    /**
     * Invalid AVD name
     * 
     */
    public static String INVALID_AVD_NAME() {
        return holder.format("INVALID_AVD_NAME");
    }

    /**
     * Invalid AVD name
     * 
     */
    public static Localizable _INVALID_AVD_NAME() {
        return new Localizable(holder, "INVALID_AVD_NAME");
    }

    /**
     * Unrecognised executable
     * 
     */
    public static String INVALID_EXECUTABLE() {
        return holder.format("INVALID_EXECUTABLE");
    }

    /**
     * Unrecognised executable
     * 
     */
    public static Localizable _INVALID_EXECUTABLE() {
        return new Localizable(holder, "INVALID_EXECUTABLE");
    }

    /**
     * Using Android SDK: {0}
     * 
     */
    public static String USING_SDK(Object arg1) {
        return holder.format("USING_SDK", arg1);
    }

    /**
     * Using Android SDK: {0}
     * 
     */
    public static Localizable _USING_SDK(Object arg1) {
        return new Localizable(holder, "USING_SDK", arg1);
    }

    /**
     * The configured Android platform needs to be installed: {0}
     * 
     */
    public static String PLATFORM_INSTALL_REQUIRED(Object arg1) {
        return holder.format("PLATFORM_INSTALL_REQUIRED", arg1);
    }

    /**
     * The configured Android platform needs to be installed: {0}
     * 
     */
    public static Localizable _PLATFORM_INSTALL_REQUIRED(Object arg1) {
        return new Localizable(holder, "PLATFORM_INSTALL_REQUIRED", arg1);
    }

    /**
     * No related app project found; cannot create build files for test project at ''{0}''.
     * 
     */
    public static String FOUND_TEST_PROJECT_WITHOUT_APP(Object arg1) {
        return holder.format("FOUND_TEST_PROJECT_WITHOUT_APP", arg1);
    }

    /**
     * No related app project found; cannot create build files for test project at ''{0}''.
     * 
     */
    public static Localizable _FOUND_TEST_PROJECT_WITHOUT_APP(Object arg1) {
        return new Localizable(holder, "FOUND_TEST_PROJECT_WITHOUT_APP", arg1);
    }

    /**
     * Failed to parse AVD config file
     * 
     */
    public static String AVD_CONFIG_NOT_READABLE() {
        return holder.format("AVD_CONFIG_NOT_READABLE");
    }

    /**
     * Failed to parse AVD config file
     * 
     */
    public static Localizable _AVD_CONFIG_NOT_READABLE() {
        return new Localizable(holder, "AVD_CONFIG_NOT_READABLE");
    }

    /**
     * Failed to restart emulator execution; cannot continue
     * 
     */
    public static String EMULATOR_RESUME_FAILED() {
        return holder.format("EMULATOR_RESUME_FAILED");
    }

    /**
     * Failed to restart emulator execution; cannot continue
     * 
     */
    public static Localizable _EMULATOR_RESUME_FAILED() {
        return new Localizable(holder, "EMULATOR_RESUME_FAILED");
    }

    /**
     * Erasing existing emulator data...
     * 
     */
    public static String ERASING_EXISTING_EMULATOR_DATA() {
        return holder.format("ERASING_EXISTING_EMULATOR_DATA");
    }

    /**
     * Erasing existing emulator data...
     * 
     */
    public static Localizable _ERASING_EXISTING_EMULATOR_DATA() {
        return new Localizable(holder, "ERASING_EXISTING_EMULATOR_DATA");
    }

    /**
     * Screen density is required
     * 
     */
    public static String SCREEN_DENSITY_REQUIRED() {
        return holder.format("SCREEN_DENSITY_REQUIRED");
    }

    /**
     * Screen density is required
     * 
     */
    public static Localizable _SCREEN_DENSITY_REQUIRED() {
        return new Localizable(holder, "SCREEN_DENSITY_REQUIRED");
    }

    /**
     * Failed to save AVD config file
     * 
     */
    public static String AVD_CONFIG_NOT_WRITEABLE() {
        return holder.format("AVD_CONFIG_NOT_WRITEABLE");
    }

    /**
     * Failed to save AVD config file
     * 
     */
    public static Localizable _AVD_CONFIG_NOT_WRITEABLE() {
        return new Localizable(holder, "AVD_CONFIG_NOT_WRITEABLE");
    }

    /**
     * Cannot start Android emulator: {0}
     * 
     */
    public static String CANNOT_START_EMULATOR(Object arg1) {
        return holder.format("CANNOT_START_EMULATOR", arg1);
    }

    /**
     * Cannot start Android emulator: {0}
     * 
     */
    public static Localizable _CANNOT_START_EMULATOR(Object arg1) {
        return new Localizable(holder, "CANNOT_START_EMULATOR", arg1);
    }

    /**
     * Run Android monkey tester
     * 
     */
    public static String RUN_MONKEY() {
        return holder.format("RUN_MONKEY");
    }

    /**
     * Run Android monkey tester
     * 
     */
    public static Localizable _RUN_MONKEY() {
        return new Localizable(holder, "RUN_MONKEY");
    }

    /**
     * Cannot automatically install unrecognised Android add-on: {0}
     * 
     */
    public static String SDK_ADDON_FORMAT_UNRECOGNISED(Object arg1) {
        return holder.format("SDK_ADDON_FORMAT_UNRECOGNISED", arg1);
    }

    /**
     * Cannot automatically install unrecognised Android add-on: {0}
     * 
     */
    public static Localizable _SDK_ADDON_FORMAT_UNRECOGNISED(Object arg1) {
        return new Localizable(holder, "SDK_ADDON_FORMAT_UNRECOGNISED", arg1);
    }

    /**
     * Crashed after {0} of {1} events
     * 
     */
    public static String MONKEY_RESULT_CRASH(Object arg1, Object arg2) {
        return holder.format("MONKEY_RESULT_CRASH", arg1, arg2);
    }

    /**
     * Crashed after {0} of {1} events
     * 
     */
    public static Localizable _MONKEY_RESULT_CRASH(Object arg1, Object arg2) {
        return new Localizable(holder, "MONKEY_RESULT_CRASH", arg1, arg2);
    }

    /**
     * Detected failures in monkey output; setting build result to {0}
     * 
     */
    public static String MONKEY_SETTING_RESULT(Object arg1) {
        return holder.format("MONKEY_SETTING_RESULT", arg1);
    }

    /**
     * Detected failures in monkey output; setting build result to {0}
     * 
     */
    public static Localizable _MONKEY_SETTING_RESULT(Object arg1) {
        return new Localizable(holder, "MONKEY_SETTING_RESULT", arg1);
    }

    /**
     * Project file ''{0}'' requires platform ''{1}''
     * 
     */
    public static String PROJECT_HAS_PLATFORM(Object arg1, Object arg2) {
        return holder.format("PROJECT_HAS_PLATFORM", arg1, arg2);
    }

    /**
     * Project file ''{0}'' requires platform ''{1}''
     * 
     */
    public static Localizable _PROJECT_HAS_PLATFORM(Object arg1, Object arg2) {
        return new Localizable(holder, "PROJECT_HAS_PLATFORM", arg1, arg2);
    }

    /**
     * Setting hardware properties:
     * 
     */
    public static String SETTING_HARDWARE_PROPERTIES() {
        return holder.format("SETTING_HARDWARE_PROPERTIES");
    }

    /**
     * Setting hardware properties:
     * 
     */
    public static Localizable _SETTING_HARDWARE_PROPERTIES() {
        return new Localizable(holder, "SETTING_HARDWARE_PROPERTIES");
    }

    /**
     * Failed to evaluate XPath for manifest at ''{0}''. Please report this as a bug!
     * 
     */
    public static String MANIFEST_XPATH_FAILURE(Object arg1) {
        return holder.format("MANIFEST_XPATH_FAILURE", arg1);
    }

    /**
     * Failed to evaluate XPath for manifest at ''{0}''. Please report this as a bug!
     * 
     */
    public static Localizable _MANIFEST_XPATH_FAILURE(Object arg1) {
        return new Localizable(holder, "MANIFEST_XPATH_FAILURE", arg1);
    }

    /**
     * Failure
     * 
     */
    public static String BUILD_RESULT_FAILURE() {
        return holder.format("BUILD_RESULT_FAILURE");
    }

    /**
     * Failure
     * 
     */
    public static Localizable _BUILD_RESULT_FAILURE() {
        return new Localizable(holder, "BUILD_RESULT_FAILURE");
    }

    /**
     * Waiting for the configured Android emulator to become available
     * 
     */
    public static String WAITING_FOR_EMULATOR() {
        return holder.format("WAITING_FOR_EMULATOR");
    }

    /**
     * Waiting for the configured Android emulator to become available
     * 
     */
    public static Localizable _WAITING_FOR_EMULATOR() {
        return new Localizable(holder, "WAITING_FOR_EMULATOR");
    }

    /**
     * Waiting {0} seconds before starting emulator...
     * 
     */
    public static String DELAYING_START_UP(Object arg1) {
        return holder.format("DELAYING_START_UP", arg1);
    }

    /**
     * Waiting {0} seconds before starting emulator...
     * 
     */
    public static Localizable _DELAYING_START_UP(Object arg1) {
        return new Localizable(holder, "DELAYING_START_UP", arg1);
    }

    /**
     * Publish Android monkey tester result
     * 
     */
    public static String PUBLISH_MONKEY_OUTPUT() {
        return holder.format("PUBLISH_MONKEY_OUTPUT");
    }

    /**
     * Publish Android monkey tester result
     * 
     */
    public static Localizable _PUBLISH_MONKEY_OUTPUT() {
        return new Localizable(holder, "PUBLISH_MONKEY_OUTPUT");
    }

    /**
     * Stopping Android emulator
     * 
     */
    public static String STOPPING_EMULATOR() {
        return holder.format("STOPPING_EMULATOR");
    }

    /**
     * Stopping Android emulator
     * 
     */
    public static Localizable _STOPPING_EMULATOR() {
        return new Localizable(holder, "STOPPING_EMULATOR");
    }

    /**
     * The Android SDK is not available for {0}
     * 
     */
    public static String SDK_UNAVAILABLE(Object arg1) {
        return holder.format("SDK_UNAVAILABLE", arg1);
    }

    /**
     * The Android SDK is not available for {0}
     * 
     */
    public static Localizable _SDK_UNAVAILABLE(Object arg1) {
        return new Localizable(holder, "SDK_UNAVAILABLE", arg1);
    }

    /**
     * app
     * 
     */
    public static String PROJECT_TYPE_APP() {
        return holder.format("PROJECT_TYPE_APP");
    }

    /**
     * app
     * 
     */
    public static Localizable _PROJECT_TYPE_APP() {
        return new Localizable(holder, "PROJECT_TYPE_APP");
    }

    /**
     * Android SDK installation failed
     * 
     */
    public static String SDK_INSTALLATION_FAILED() {
        return holder.format("SDK_INSTALLATION_FAILED");
    }

    /**
     * Android SDK installation failed
     * 
     */
    public static Localizable _SDK_INSTALLATION_FAILED() {
        return new Localizable(holder, "SDK_INSTALLATION_FAILED");
    }

    /**
     * Cannot find Android SDK at ''{0}''
     * 
     */
    public static String SDK_NOT_FOUND(Object arg1) {
        return holder.format("SDK_NOT_FOUND", arg1);
    }

    /**
     * Cannot find Android SDK at ''{0}''
     * 
     */
    public static Localizable _SDK_NOT_FOUND(Object arg1) {
        return new Localizable(holder, "SDK_NOT_FOUND", arg1);
    }

    /**
     * Cannot find desired platform image at ''{0}''
     * 
     */
    public static String PLATFORM_IMAGE_NOT_FOUND(Object arg1) {
        return holder.format("PLATFORM_IMAGE_NOT_FOUND", arg1);
    }

    /**
     * Cannot find desired platform image at ''{0}''
     * 
     */
    public static Localizable _PLATFORM_IMAGE_NOT_FOUND(Object arg1) {
        return new Localizable(holder, "PLATFORM_IMAGE_NOT_FOUND", arg1);
    }

    /**
     * There is more than one system image defined for platform ''{0}''.
     * Pick an image to use and set it in the ''Target ABI'' config field.
     * {1}.
     * 
     */
    public static String MORE_THAN_ONE_ABI(Object arg1, Object arg2) {
        return holder.format("MORE_THAN_ONE_ABI", arg1, arg2);
    }

    /**
     * There is more than one system image defined for platform ''{0}''.
     * Pick an image to use and set it in the ''Target ABI'' config field.
     * {1}.
     * 
     */
    public static Localizable _MORE_THAN_ONE_ABI(Object arg1, Object arg2) {
        return new Localizable(holder, "MORE_THAN_ONE_ABI", arg1, arg2);
    }

    /**
     * Installing the ''{0}'' SDK component(s)...
     * 
     */
    public static String INSTALLING_SDK_COMPONENTS(Object arg1) {
        return holder.format("INSTALLING_SDK_COMPONENTS", arg1);
    }

    /**
     * Installing the ''{0}'' SDK component(s)...
     * 
     */
    public static Localizable _INSTALLING_SDK_COMPONENTS(Object arg1) {
        return new Localizable(holder, "INSTALLING_SDK_COMPONENTS", arg1);
    }

    /**
     * Android add-on name looks incorrect: {0}
     * 
     */
    public static String SDK_ADDON_NAME_INCORRECT(Object arg1) {
        return holder.format("SDK_ADDON_NAME_INCORRECT", arg1);
    }

    /**
     * Android add-on name looks incorrect: {0}
     * 
     */
    public static Localizable _SDK_ADDON_NAME_INCORRECT(Object arg1) {
        return new Localizable(holder, "SDK_ADDON_NAME_INCORRECT", arg1);
    }

    /**
     * SDK platforms directory appears to be empty.  See inline help for info
     * 
     */
    public static String SDK_PLATFORMS_EMPTY() {
        return holder.format("SDK_PLATFORMS_EMPTY");
    }

    /**
     * SDK platforms directory appears to be empty.  See inline help for info
     * 
     */
    public static Localizable _SDK_PLATFORMS_EMPTY() {
        return new Localizable(holder, "SDK_PLATFORMS_EMPTY");
    }

    /**
     * The desired AVD platform ''{0}'' is not installed on this machine
     * 
     */
    public static String INVALID_AVD_TARGET(Object arg1) {
        return holder.format("INVALID_AVD_TARGET", arg1);
    }

    /**
     * The desired AVD platform ''{0}'' is not installed on this machine
     * 
     */
    public static Localizable _INVALID_AVD_TARGET(Object arg1) {
        return new Localizable(holder, "INVALID_AVD_TARGET", arg1);
    }

    /**
     * Waiting for emulator to finish booting...
     * 
     */
    public static String WAITING_FOR_BOOT_COMPLETION() {
        return holder.format("WAITING_FOR_BOOT_COMPLETION");
    }

    /**
     * Waiting for emulator to finish booting...
     * 
     */
    public static Localizable _WAITING_FOR_BOOT_COMPLETION() {
        return new Localizable(holder, "WAITING_FOR_BOOT_COMPLETION");
    }

    /**
     * Unfortunately this particular package cannot be automatically installed on SDK Tools r15 or earlier...
     * 
     */
    public static String SDK_ADDON_INSTALLATION_UNSUPPORTED() {
        return holder.format("SDK_ADDON_INSTALLATION_UNSUPPORTED");
    }

    /**
     * Unfortunately this particular package cannot be automatically installed on SDK Tools r15 or earlier...
     * 
     */
    public static Localizable _SDK_ADDON_INSTALLATION_UNSUPPORTED() {
        return new Localizable(holder, "SDK_ADDON_INSTALLATION_UNSUPPORTED");
    }

    /**
     * Locale should have format: ab_XY
     * 
     */
    public static String LOCALE_FORMAT_WARNING() {
        return holder.format("LOCALE_FORMAT_WARNING");
    }

    /**
     * Locale should have format: ab_XY
     * 
     */
    public static Localizable _LOCALE_FORMAT_WARNING() {
        return new Localizable(holder, "LOCALE_FORMAT_WARNING");
    }

    /**
     * Going to install required Android SDK components...
     * 
     */
    public static String INSTALLING_REQUIRED_COMPONENTS() {
        return holder.format("INSTALLING_REQUIRED_COMPONENTS");
    }

    /**
     * Going to install required Android SDK components...
     * 
     */
    public static Localizable _INSTALLING_REQUIRED_COMPONENTS() {
        return new Localizable(holder, "INSTALLING_REQUIRED_COMPONENTS");
    }

}
