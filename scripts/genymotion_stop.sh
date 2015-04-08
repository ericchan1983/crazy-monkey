#!/bin/bash

vm_name=$1

ps aux | grep "genymotion/player --vm-name ${vm_name}" | grep -v grep | awk '{print $2}' | xargs -rt kill -9

ps aux | grep "virtualbox/VBoxHeadless --comment ${vm_name}" | grep -v grep | awk '{print $2}' | xargs -rt kill -9

echo "Success"
