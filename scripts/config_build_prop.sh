#!/bin/bash
device_Sn=$1
vm_name=$2

function getValue () {
	field=$1
	vm_name=$2
	
	value=`jq .$field $CRAZY_MONKEY_HOME/userdata/xposeDevice_$vm_name.txt | awk -F "\"" '{print $2}'`
	echo $value
}

function setProp () {
	device_Sn=$1
	field=$2
	property=$3
	vm_name=$4

	value=`getValue ${field} ${vm_name}`
	echo "set $property=$value on the device '$device_Sn'"
	$ANDROID_SDK_HOME/platform-tools/adb -s ${device_Sn} shell sed -i "s/^$property=.*/$property=$value/g" /system/build.prop
}

function addProp () {
	device_Sn=$1
	field=$2
	property=$3
	vm_name=$4

	value=`getValue ${field} ${vm_name}`
	echo "add $property=$value on the device '$device_Sn'"
	$ANDROID_SDK_HOME/platform-tools/adb -s ${device_Sn} shell "echo $property=$value >> /system/build.prop"
}

function writeBuildProp () {
	device_Sn=$1
	vm_name=$2

	$ANDROID_SDK_HOME/platform-tools/adb -s $device_Sn remount

	setProp $device_Sn BRAND ro.product.brand $vm_name
	setProp $device_Sn MANUFACTURER ro.product.manufacturer $vm_name
	setProp $device_Sn DEVICE ro.product.name $vm_name
	setProp $device_Sn DEVICE ro.product.device $vm_name
	setProp $device_Sn DEVICE ro.build.product $vm_name
	setProp $device_Sn SDK ro.build.version.sdk $vm_name
	setProp $device_Sn RELEASE ro.build.version.release $vm_name
	setProp $device_Sn FINGERPRINT ro.build.fingerprint $vm_name
	setProp $device_Sn FINGERPRINT ro.build.display.id $vm_name
	addProp $device_Sn MODEL ro.product.model $vm_name
	
	echo "change ro.build.id=JWR66Y on the device '$device_Sn'"
	$ANDROID_SDK_HOME/platform-tools/adb -s $device_Sn shell sed -i 's/^ro.build.id=.*/ro.build.id=JWR66Y/g' /system/build.prop
	#echo "change ro.build.display.id=JWR66Y on the device '$device_Sn'"
	#$ANDROID_SDK_HOME/platform-tools/adb -s $device_Sn shell sed -i 's/^ro.build.display.id=.*/ro.build.display.id=JWR66Y/g' /system/build.prop
}

writeBuildProp $device_Sn $vm_name
/bin/bash $CRAZY_MONKEY_HOME/scripts/genymotion_stop.sh $vm_name && echo "Success."

exit 0
