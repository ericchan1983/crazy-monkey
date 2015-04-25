#!/bin/bash
$ANDROID_SDK_HOME/platform-tools/adb -s $1 push $CRAZY_MONKEY_HOME/userdata/xposeDevice_$2.txt /mnt/sdcard/.system
$ANDROID_SDK_HOME/platform-tools/adb -s $1 shell mv /mnt/sdcard/.system/xposeDevice_$2.txt /mnt/sdcard/.system/xposeDevice.txt
sleep 3
timeout 150 $ANDROID_SDK_HOME/platform-tools/adb -s $1 shell am instrument -w com.crazymonkey.test009/android.test.InstrumentationTestRunner