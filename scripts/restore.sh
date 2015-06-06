#!/system/bin/sh
package=$1

if [ ! -d "/mnt/sdcard/backup" ]; then
	mkdir /mnt/sdcard/backup
fi 

if [ ! -d "/mnt/sdcard/backup/userdata" ]; then
	mkdir /mnt/sdcard/backup/userdata
fi 

if [ ! -d "/mnt/sdcard/backup/shell" ]; then
	mkdir /mnt/sdcard/backup/shell
fi 

if [ -d "/mnt/sdcard/backup/shell/mnt" ]; then
	rm -r /mnt/sdcard/backup/shell/mnt
fi 

echo "move the userdata into backup folder"
mv /mnt/sdcard/userdata.tar.gz /mnt/sdcard/backup/userdata.tar.gz
echo "move all the files from the userdata"
rm -r /mnt/sdcard/backup/userdata/*
echo "exact the userdata"
tar xzvf /mnt/sdcard/backup/userdata.tar.gz -C /mnt/sdcard/backup/shell/
echo "cpoy the userdata to backup"
cp -r /mnt/sdcard/backup/shell/mnt/sdcard/backup/userdata/*  /mnt/sdcard/backup/userdata/
echo "copy the userdata into apk folder"
cp -r /mnt/sdcard/backup/userdata/* /data/data/$package/
echo "add the permission"
chmod -R 777 /data/data/$package/*