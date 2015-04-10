#!/bin/bash

vm_name=$1

VBoxManage snapshot $vm_name restore "factory"

# screen VBoxManage -startvm $vm_name
screen $CRAZY_MONKEY_HOME/../genymotion/player --vm-name $vm_name

exit 0
