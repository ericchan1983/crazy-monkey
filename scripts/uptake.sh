#!/bin/bash
function restore () {
	try_times=0
	while [ $try_times -lt 20 ]
	do 
		VBoxManage snapshot "Android_Monkey_${try_times}" restore "factory"
		try_times=`expr $try_times + 1`
	done
}

function take () {
	try_times=0
	while [ $try_times -lt 20 ]
	do 
		VBoxManage snapshot "Android_Monkey_${try_times}" take "factory_009"
		try_times=`expr $try_times + 1`
	done
}

function update () {
	package=$1
	apk_path=$2
	adb devices | grep "5555" | awk '{print $1}' | xargs -I {sn} adb -s {sn} uninstall $1
	adb devices | grep "5555" | awk '{print $1}' | xargs -I {sn} adb -s {sn} install ${apk_path}	
}

cmd=$1
package=$2
path=$3

if [ "${cmd}" == "r" ]
then
	restore
elif [ "${cmd}" == "t" ]
then
	take
elif [ "${cmd}" == "u" ]
	update $package $path
fi
exit 0
