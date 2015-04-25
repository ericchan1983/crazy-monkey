#!/bin/bash

vm_name=$1
is_restore=$2
if [ -n "$is_restore" ] && [ "$is_restore" != "false" ]; then
	VBoxManage snapshot $vm_name restore "factory_2"
fi

# screen VBoxManage -startvm $vm_name
nohup $CRAZY_MONKEY_HOME/../genymotion/player --vm-name $vm_name &

exit 0
