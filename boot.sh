#!/bin/bash
cd /home/android/crazy-monkey/crazy-monkey
ps aux | grep "/bin/bash ./checkAdb.sh" | grep -v grep | awk '{print $2}' | xargs -rt sudo kill -9
/bin/bash ./checkAdb.sh &
/bin/bash ./run.sh &> ./logs/console_`date '+%Y-%m-%dT%H-%M-%S'`_log &
