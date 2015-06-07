#!/system/bin/sh
brand=$1
manufacturer=$2
name=$3
device=$4
product=$5
sdk=$6
release=$7
fingerprint=$8
display=$9
model=$10

echo "brand = $brand"
sed -i "s#^ro.product.brand=.*#ro.product.brand=$brand#g" /system/build.prop
echo "manufacturer = $manufacturer"
sed -i "s#^ro.product.manufacturer=.*#ro.product.manufacturer=$manufacturer#g" /system/build.prop
echo "name = $name"
sed -i "s#^ro.product.name=.*#ro.product.name=$name#g" /system/build.prop
echo "device = $device"
sed -i "s#^ro.product.device=.*#ro.product.device=$device#g" /system/build.prop
echo "product = $product"
sed -i "s#^ro.build.product=.*#ro.build.product=$product#g" /system/build.prop
echo "sdk = $sdk"
sed -i "s#^ro.build.version.sdk=.*#ro.build.version.sdk=$sdk#g" /system/build.prop
echo "release = $release"
sed -i "s#^ro.build.version.release=.*#ro.build.version.release=$release#g" /system/build.prop
echo "fingerprint = $fingerprint"
sed -i "s#^ro.build.fingerprint=.*#ro.build.fingerprint=$fingerprint#g" /system/build.prop
echo "display.id = $display"
sed -i "s#^ro.build.display.id=.*#ro.build.display.id=$display#g" /system/build.prop
echo "model = $model"
echo "ro.product.model=$model" >> /system/build.prop

sed -i 's/^ro.build.id=.*/ro.build.id=JWR66Y/g' /system/build.prop