#!/bin/bash

echo "Checking Adb service"

sleep 600

source ./setenv.sh

while true
do
	adb devices && result=1 || result=0
	if [ "$result" = "0" ]; then
		#echo "Restarting Crazy Monkey since Adb crashed..."
		#cd $CRAZY_MONKEY_HOME
		#nohup ./boot.sh
		#exit 0	
		echo "Restarting the adb server"
                pgrep adb | xargs -rt kill -9
                adb start-server
                adb devices
	fi
	echo "Sleep 600s"
	sleep 600
done
