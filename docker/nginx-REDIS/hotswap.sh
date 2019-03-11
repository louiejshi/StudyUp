#!/bin/bash
old=`awk '/6379/{print $2}' /etc/nginx/nginx.conf | awk -F ":" '/6379/{print $1}'`
new=$1
echo 'new: '$new
echo 'old:' $old
if [ "$1" == "$old" ]
then
        echo "Don't need to reload!!!"
else
        sed -i "/6379/s/$old/$new/g" /etc/nginx/nginx.conf
        echo "Reload"
        /usr/sbin/nginx -s reload
fi
