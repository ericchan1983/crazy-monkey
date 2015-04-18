#!/bin/bash
$ANDROID_SDK_HOME/platform-tools/adb -s $1 install $CRAZY_MONKEY_HOME/scripts/SuperVPN.apk
$ANDROID_SDK_HOME/tools/monkeyrunner $CRAZY_MONKEY_HOME/scripts/connect_vpn.py $1