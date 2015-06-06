#!/system/bin/sh
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

mv /mnt/sdcard/userdata.tar.gz /mnt/sdcard/backup/userdata.tar.gz
rm -r /mnt/sdcard/backup/shell/mnt
rm -r /mnt/sdcard/backup/userdata/*
tar xzvf /mnt/sdcard/backup/userdata.tar.gz -C /mnt/sdcard/backup/shell/
cp -r /mnt/sdcard/backup/shell/mnt/sdcard/backup/userdata/*  /mnt/sdcard/backup/userdata/
cp -r /mnt/sdcard/backup/userdata/* /data/data/$package/
chmod -R 777 /data/data/$package/*