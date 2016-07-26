#! /bin/sh

USER=$1
PASSWD=$2

sudo useradd -g sftp -d /home/"$USER" -s /sbin/nologin "$USER" 

echo "$USER:$PASSWD" | chpasswd

sudo chown root:root /home/"$USER"

sudo chmod 755 /home/"$USER"

sudo service sshd restart







