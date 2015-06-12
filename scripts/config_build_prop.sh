#!/bin/bash
device_Sn=$1
vm_name=$2

function getValue () {
	field=$1
	vm_name=$2
	value=`jq .$field $CRAZY_MONKEY_HOME/userdata/xposeDevice_$vm_name.txt | awk -F "\"" '{print $2}'`
	echo $value
}

function writeBuildProp () {

	device_Sn=$1
	vm_name=$2

	brand=`getValue BRAND ${vm_name}`
	manufacturer=`getValue MANUFACTURER ${vm_name}`
	name=`getValue DEVICE ${vm_name}`
	device=$name
	product=$name
	sdk=`getValue SDK ${vm_name}`
	release=`getValue RELEASE ${vm_name}`
	fingerprint=`getValue FINGERPRINT ${vm_name}`
	display=$fingerprint
	model=`getValue MODEL ${vm_name}`
	
	$ANDROID_SDK_HOME/platform-tools/adb -s $device_Sn remount
	$ANDROID_SDK_HOME/platform-tools/adb -s $device_Sn push $CRAZY_MONKEY_HOME/scripts/config_model.sh /mnt/sdcard/config_model.sh
	$ANDROID_SDK_HOME/platform-tools/adb -s $device_Sn shell "sh /mnt/sdcard/config_model.sh '$brand' '$manufacturer' '$name' '$device' '$product' '$sdk' '$release' '$fingerprint' '$display' '$model'"
	
}

writeBuildProp $device_Sn $vm_name
/bin/bash $CRAZY_MONKEY_HOME/scripts/genymotion_stop.sh $vm_name && echo "Success."

exit 0