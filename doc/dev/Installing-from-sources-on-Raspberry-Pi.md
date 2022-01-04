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

## Update + upgrade Raspbian
It's a good practice to update and upgrade the libraries of Raspberry Pi before continuing.
```bash
sudo apt-get update
sudo apt-get upgrade
```

## Install npm
```bash
wget https://nodejs.org/dist/v16.13.1/node-v16.13.1-linux-armv7l.tar.xz
tar -xvf node-v16.13.1-linux-armv7l.tar.xz
rm -rf node-v16.13.1-linux-armv7l.tar.xz
sudo cp -r node-v16.13.1-linux-armv7l/{bin,include,lib,share} /usr/
rm -rf node-v16.13.1-linux-armv7l
node --version
```

## Getting sources
I'm proposing to run this script from "/home/pi" (that's the easiest way to install AE)
```bash
git clone https://github.com/tomaszbabiuk/automate-everything.git
```

## Building from sources
"Automate Everything" needs gradle and npm to build. The most usable scripts and gradle tasks are:
Run all the commands from the main directory of the project.


* For building the frontend (user interface):
```
cd automate-everything
npm install --prefix ae-frontend
npm run build --prefix ae-frontend
```

* For building the backend (the automation server + plugins):
```
./gradlew :ae-backend:assembleBackend
```

* For (re)building plugins only:
```
./gradlew :plugins:assemblePlugins
```

### Directory structure
After building, the directory structure should look like this:
```
output
    |- bin
    |   - ae-backend-all.jar
    |- plugins
    |   - plugin1.jar
    |   - plugin2.jar
    |   - enabled.txt
    | - web
    |   - css
    |       - app.*.css
    |       - chunk-vendors.*.css
    |   - js
    |       - app.*.js
    |       - app.*.js.map
    |       - chunk-vendors.*.js
    |       - chunk-vendors.*.js.map
    |   - media
    |       - (all media files required by blockly)
    |   - favicon.ico
    |   - index.html
```

## Running
* To run the server run:
```
cd output
java -jar bin/ae-backend-all.jar
```
* or in debug mode:
```
cd output
java -Dorg.slf4j.simpleLogger.defaultLogLevel=debug -jar bin/ae-backend-all.jar
```
After starting, open *http://localhost* in your browser

## Running in slow mode (for UI testing purpose)
When using this option, all requests are going to be delayed for 5 seconds (good for UI testing).
```
cd output
java -jar bin/ae-backend-all.jar -slow
```
After starting, open *http://localhost* in your browser