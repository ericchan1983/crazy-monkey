#!/bin/bash

cd /home/android/crazy-monkey/crazy-monkey

function stopMonitorScripts () {
	echo "[Crazy Monkey] Kill the check_adb script"
	ps aux | grep "/bin/bash ./check_adb.sh" | grep -v grep | awk '{print $2}' | xargs -rt sudo kill -9
	echo "[Crazy Monkey] Kill the check_status script"
	ps aux | grep "/bin/bash ./check_status.sh" | grep -v grep | awk '{print $2}' | xargs -rt sudo kill -9
	echo "[Crazy Monkey] Kill the update_apk script"
	ps aux | grep "/bin/bash ./update_apk.sh" | grep -v grep | awk '{print $2}' | xargs -rt sudo kill -9
}

stopMonitorScripts

/bin/bash ./check_adb.sh &> ./console/check_adb_`date '+%Y-%m-%dT%H-%M-%S'`_log &

/bin/bash ./check_status.sh &> ./console/check_status_`date '+%Y-%m-%dT%H-%M-%S'`_log &

/bin/bash ./update_apk.sh &> ./console/update_apk_`date '+%Y-%m-%dT%H-%M-%S'`_log &

/bin/bash ./run.sh &> ./console/console_`date '+%Y-%m-%dT%H-%M-%S'`_log &