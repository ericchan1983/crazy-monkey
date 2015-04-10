#!/bin/bash
sleep 20

vm_name=$1
try_times=0

while [ $try_times -lt 5 ]
do 
	IP=`VBoxManage guestproperty get "${vm_name}" "androvm_ip_management" | awk '{print $2}'`
	if [ -n "$IP" ] && [ "$IP" != "value" ]; then
		$ANDROID_SDK_HOME/platform-tools/adb connect $IP:5555 && result=1 || result=0
		if [ "$result" = "1" ]; then 
			$ANDROID_SDK_HOME/platform-tools/adb -s $IP:5555 shell echo READY && result=1 || result=0
			if [ "$result" = "1" ]; then
				echo $IP:5555 > $CRAZY_MONKEY_HOME/userdata/genymotion_$vm_name.ini
				echo "Success"
				break
			fi
		fi
	fi
	try_times=`expr $try_times + 1`
	sleep 20
done
exit 0
