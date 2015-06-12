#!/bin/bash

echo "Checking APK service"

source ./setenv.sh

while true
do
	cd $CRAZY_MONKEY_HOME/apk && git pull && result=1 || result=0
	
	if [ "$result" = "1" ]; then
		echo "Update the scripts folder successfully"
	else
		echo "Update the scripts folder failed"
	fi
	echo "Sleep 600s"
	sleep 600
done