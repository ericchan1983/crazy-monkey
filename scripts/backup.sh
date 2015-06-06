#!/bin/bash
package=$1

if [ ! -d "/mnt/sdcard/backup"]; then
	mkdir /mnt/sdcard/backup
fi 

if [ ! -d "/mnt/sdcard/backup/userdata"]; then
	mkdir /mnt/sdcard/backup/userdata
fi 

if [ ! -d "/mnt/sdcard/backup/shell"]; then
	mkdir /mnt/sdcard/backup/shell
fi 

cp -r /data/data/$package/* /mnt/sdcard/backup/userdata/
rm -r /mnt/sdcard/backup/userdata/cache/
rm -r /mnt/sdcard/backup/userdata/lib/
tar czvf /mnt/sdcard/backup/userdata.tar.gz /mnt/sdcard/backup/userdata/*
