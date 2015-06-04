#!/bin/bash

cd /home/android/crazy-monkey/crazy-monkey

ps aux | grep "/bin/bash ./check_adb.sh" | grep -v grep | awk '{print $2}' | xargs -rt sudo kill -9

ps aux | grep "/bin/bash ./check_status.sh" | grep -v grep | awk '{print $2}' | xargs -rt sudo kill -9

/bin/bash ./check_adb.sh &> ./console/check_adb_`date '+%Y-%m-%dT%H-%M-%S'`_log &

/bin/bash ./check_status.sh &> ./console/check_status_`date '+%Y-%m-%dT%H-%M-%S'`_log &

/bin/bash ./run.sh &> ./console/console_`date '+%Y-%m-%dT%H-%M-%S'`_log &