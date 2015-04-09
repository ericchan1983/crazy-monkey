#!/bin/bash
sleep 10

vm_name=$1
time=0
while [ $time -lt 5 ]
do 
	IP=`VBoxManage guestproperty get "${vm_name}" "androvm_ip_management" | awk '{print $2}'`
	if [ -n "$IP" ] && [ "$IP" != "value" ]; then
		$ANDROID_SDK_HOME/platform-tools/adb -s $IP:5555 shell echo READY && result=1 || result=0
		if [ "$result" = "1" ]; then
			echo $IP:5555 > $CRAZY_MONKEY_HOME/userdata/genymotion_$vm_name.ini
			echo "Success"
			break
		fi
	fi
	time=`expr $time + 1`
	sleep 10
done
exit 0
