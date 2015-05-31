#!/bin/bash

echo "Checking Adb service"

sleep 600

source ./setenv.sh

while true
do
	adb devices && result=1 || result=0
	if [ "$result" = "0" ]; then
		echo "Restarting Crazy Monkey since Adb crashed..."
		./boot.sh
		exit 0
	fi
	echo "Sleep 1800s"
	sleep 1800
done
exit 0
