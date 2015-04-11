#!/bin/bash

vm_name=$1
ps aux | grep "genymotion/player --vm-name ${vm_name}" | grep -v grep | awk '{print $2}' | xargs -rt kill -9

IP=`VBoxManage guestproperty get "${vm_name}" "androvm_ip_management" | awk '{print $2}'`
ps aux | grep "android-sdk-linux/platform-tools/adb -s ${IP}:5555" | grep -v grep | awk '{print $2}' | xargs -rt kill -9
$ANDROID_SDK_HOME/platform-tools/adb disconnect $IP:5555

# VBoxManage controlvm $vm_name acpipowerbutton
# VBoxManage controlvm $vm_name keyboardputscancode 1c
# ps aux | grep "virtualbox/VBoxHeadless --comment ${vm_name}" | grep -v grep | awk '{print $2}' | xargs -rt kill -9

VBoxManage controlvm $vm_name poweroff soft && echo "Success"

exit 0
