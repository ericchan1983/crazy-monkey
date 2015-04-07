#!/bin/bash
sleep 15

MAC=`VBoxManage showvminfo ${vm_name} | grep MAC | grep vboxnet0 | awk -F ":" '{print $3}' | cut -c 2-13`

MAC=`echo ${MAC} | awk  'BEGIN{FS=""}{print $1$2":"$3$4":"$5$6":"$7$8":"$9$10":"$11$12}'`

IP=`arp -a | grep -i ${MAC} | grep -E -o  "([0-9]{1,3}[\.]){3}[0-9]{1,3}"`

ping_test=`ping ${IP} -c 5 | grep rtt | sed 's/rtt //g'`

if [ -n "$ping_test" ] 
then	
	echo $IP:5555 > $CRAZY_MONKEY_HOME/userdata/genymotion_$1.ini
	echo "Success"
else
	echo "Failure"
fi
