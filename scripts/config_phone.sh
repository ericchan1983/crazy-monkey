#!/bin/bash
$ANDROID_SDK_HOME/platform-tools/adb -s $1 push $CRAZY_MONKEY_HOME/userdata/xposeDevice.txt /mnt/sdcard/.system
timeout 240 $ANDROID_SDK_HOME/platform-tools/adb -s $1 shell am instrument -w com.crazymonkey.test009/android.test.InstrumentationTestRunner