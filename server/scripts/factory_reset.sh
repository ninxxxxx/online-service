#!/bin/sh

mkdir -p /home/android/root/cache/recovery
echo '--wipe_data' > /home/android/root/cache/recovery/command
rm -rf /mnt/mydata/Android/data
sync
echo "Factory reset complete ==> Rebooting"
reboot
