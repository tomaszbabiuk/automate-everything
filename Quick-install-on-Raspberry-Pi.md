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

### Java troubleshooting.
If you have problem running multiple java versions, or getting errors like that:
```
Error occurred during initialization of VM
Server VM is only supported on ARMv7+ VFP
```
run
```bash 
sudo update-alternatives --config java
```
and manually select java 11

## Getting release files