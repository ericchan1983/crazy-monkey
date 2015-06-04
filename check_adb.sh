#!/bin/bash

echo "Checking Adb service"

sleep 900

source ./setenv.sh

while true
do
	adb devices && result=1 || result=0
	if [ "$result" = "0" ]; then
		echo "Restarting Crazy Monkey since Adb crashed..."
		cd $CRAZY_MONKEY_HOME
		nohup ./boot.sh
		exit 0
	fi
	echo "Sleep 700s"
	sleep 700
done
