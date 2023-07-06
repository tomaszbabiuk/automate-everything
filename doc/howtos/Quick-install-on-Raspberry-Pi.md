# Installing on Raspberry Pi
The recommended version of Raspberry Pi is *Raspberry Pi 4*.
The setup below assumes you have a fresh installation of Raspbian/Ubuntu and you can connect to the board with SSH:
```bash
ssh pi@x.x.x.x
```
Where x.x.x.x is the IP address of your Pi in your local network.

## Installing java
```bash
sudo apt update
sudo apt install openjdk-11-jdk
java -version
```

## Getting release files
```bash
wget https://github.com/tomaszbabiuk/automate-everything/releases/download/v0.1.0/automate-everything-0.1.0.zip
unzip automate-everything-0.1.0.zip
rm -rf automate-everything-0.1.0.zip
```

## Manual run
```bash
cd automate-everything
sudo java -jar bin/ae-backend-all.jar
```
## Starting automation server on boot
Run this script in '/home/pi' directory

```bash
cat <<EOT >> ae.service
[Unit]
Description=Automation Everything server
After=multi-user.target

[Service]
Type=idle
WorkingDirectory=/home/pi/automate-everything
ExecStart=/usr/bin/java -jar /home/pi/automate-everything/bin/ae-backend-all.jar

[Install]
WantedBy=multi-user.target
EOT

sudo mv ae.service /lib/systemd/system
sudo touch /lib/systemd/system/ae.service
sudo systemctl daemon-reload
sudo systemctl enable ae.service
```
