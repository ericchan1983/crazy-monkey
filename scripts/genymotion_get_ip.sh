#!/bin/bash
vm_name=$1
sleep 15
# MAC=`VBoxManage showvminfo ${vm_name} | grep MAC | grep vboxnet0 | awk -F ":" '{print $3}' | cut -c 2-13`
# MAC=`echo ${MAC} | awk  'BEGIN{FS=""}{print $1$2":"$3$4":"$5$6":"$7$8":"$9$10":"$11$12}' | tr '[:upper:]' '[:lower:]'`
# MAC=`echo $MAC | sed -e 's/\([0-9A-Fa-f]\{2\}\)/\1:/g' -e 's/\(.*\):$/\1/' | tr '[:upper:]' '[:lower:]'`

# IP=`arp -a | grep -i ${MAC} | grep -E -o  "([0-9]{1,3}[\.]){3}[0-9]{1,3}"`
# IP=`VBoxManage guestproperty get "${vm_name}" "/VirtualBox/GuestInfo/Net/0/V4/IP"`

for i in {1..5}
do 
	IP=`VBoxManage guestproperty get "${vm_name}" "androvm_ip_management" | awk '{print $2}'`
	if [ -n "$IP" ] && [ "$IP" != "value" ]
	then
		ping_test=`ping ${IP} -c 3 | grep rtt | sed 's/rtt //g'`
		if [ -n "$ping_test" ]
		then
			echo $IP:5555 > $CRAZY_MONKEY_HOME/userdata/genymotion_$vm_name.ini
			echo "Success"
			break
		else
			sleep 3s
		fi
	else
		sleep 3
	fi
done
