#!/bin/bash

vm_name=$1

result=`ps aux | grep "genymotion/player --vm-name ${vm_name}" | grep -v grep | awk '{print $2}' | xargs -rt sudo kill -9`

if [ -n "$result"] 
then
	echo "Success"
else
	echo "Failure"
fi