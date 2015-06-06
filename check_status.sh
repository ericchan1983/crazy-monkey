#!/bin/bash

source ./setenv.sh

sleep 900

while true
do
	running=0

	for deviceSn in $(adb devices | grep 5555 | awk '{print $1}')
	do
		timeout 30 adb -s $deviceSn shell ps | grep "android.process.acore" >/dev/null && result=1 || result=0
		if [ "${result}" == "1" ] 
		then
			running=`expr $running + 1`
		fi
	done

	MAC_ADDRESS=`/sbin/ifconfig $NETWORK_INTERFACE | grep HWaddr | awk '{print $5}' | sed -e 's/:/-/g' |tr '[:lower:]' '[:upper:]'`

	curl --connect-timeout 20 -m 60 -G "$NODE_SERVER/slaver/setSimStatus?slaverMAC=$MAC_ADDRESS&running=$running&max=$MAX_NUM"
	echo "$running emulators is running."
	sleep 60
done
