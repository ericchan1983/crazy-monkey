#!/bin/bash

vm_name=$1

kill_player_result=`ps aux | grep "genymotion/player --vm-name ${vm_name}" | grep -v grep | awk '{print $2}' | xargs -rt sudo kill -9`

if [ -n "$kill_player_result" ] 
then
	kill_vbox_result=`ps aux | grep "virtualbox/VBoxHeadless --comment ${vm_name}" | grep -v grep | awk '{print $2}' | xargs -rt sudo kill -9`
	if [ -n "$kill_vbox_result" ]
	then
		echo "Success"
	else
		echo "Failure"
	fi
else
	echo "Failure"
fi