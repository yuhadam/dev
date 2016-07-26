#! /bin/bash

sudo groupadd sftp
sudo sed -i 's/Subsystem/#Subsystem/' /etc/ssh/sshd_config
sudo echo Subsystem	sftp	internal-sftp >> /etc/ssh/sshd_config
sudo echo Match Group sftp >> /etc/ssh/sshd_config
sudo echo	ChrootDirectory /home/%u >> /etc/ssh/sshd_config
sudo echo	X11Forwarding no	>> /etc/ssh/sshd_config
sudo echo	AllowTcpForwarding no	>> /etc/ssh/sshd_config
sudo echo 	FroceCommand internal-sftp >> /etc/ssh/sshd_config
sudo service sshd restart


