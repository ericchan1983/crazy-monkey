#!/bin/bash
# set the env

export USER=android
export NETWORK_INTERFACE=eth0
export DEFAULT_GATEWAY=192.168.3.1
export EMULATOR_TYPE=GENY_MOTION
export NODE_SERVER=http://192.168.3.2:3000
export MAX_NUM=24

export PROJECT_HOME=/home/$USER/crazy-monkey
export JAVA_HOME=$PROJECT_HOME/jdk1.7.0_75
export ANT_HOME=$PROJECT_HOME/apache-ant-1.9.4
export ANDROID_SDK_HOME=$PROJECT_HOME/android-sdk-linux
export GENY_MOTION_HOME=$PROJECT_HOME/genymotion

export VPN_CLINET_HOME=$PROJECT_HOME/vpn-client
export CRAZY_MONKEY_HOME=$PROJECT_HOME/crazy-monkey

export JRE_HOME=$JAVA_HOME/jre
export CLASSPATH=.:$JAVA_HOME/lib:$JRE_HOME/lib:$CLASSPATH

export DISPLAY=:0.0
export LD_LIBRARY_PATH=$ANDROID_SDK_HOME/tools/lib

export PATH=$JAVA_HOME/bin:$JRE_HOME/bin:$ANDROID_SDK_HOME/tools:$ANDROID_SDK_HOME/platform-tools:$ANT_HOME/bin:$GENY_MOTION_HOME:$PATH
