#!/bin/bash

vm_name=$1

vboxmanage snapshot $vm_name restore "factory"

$CRAZY_MONKEY_HOME/../genymotion/player --vm-name $vm_name & > /dev/null 2>&1
