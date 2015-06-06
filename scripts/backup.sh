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

echo "copy the userdata from the apk folder"
cp -r /data/data/$package/* /mnt/sdcard/backup/userdata/
echo "remove the cache file"
rm -r /mnt/sdcard/backup/userdata/cache/
echo "remove the lib file"
rm -r /mnt/sdcard/backup/userdata/lib/
echo "compress the userdata"
tar czvf /mnt/sdcard/backup/userdata.tar.gz /mnt/sdcard/backup/userdata/*