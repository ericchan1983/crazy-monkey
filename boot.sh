#!/bin/bash
source ./setenv.sh
cd $CRAZY_MONKEY_HOME
mkdir logs
/bin/sh ./run.sh &> ./logs/console_`date '+%Y-%m-%dT%H-%M-%S'`_log