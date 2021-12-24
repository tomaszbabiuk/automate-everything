# Welcome
"Automate Everything" is a multipurpose automation server. It can control your home or garden watering system. It can even trade cryptocurrencies! 

Think of "Automate Everything" as more plugin friendly OpenHab or deeply customizable HomeAssistant. You can even use "Automate Everything" as "If This Than That" service in your local network. 

With correct set of plugins you can automate literally everything!

# Best features
1. Everything is a plugin
2. Zero-conf principle
3. Modern UI
4. Deeply integrated with Google Blockly
5. Type-safe automation
6. Different automation types (event based/ loop based)
7. Developers haven
8. More than Home Automation server
9. Embedded MQTT server

# A note of history
The predecessor of "Automate Everything" was called "GeekHOME Server". The origin project was created almost 10 years ago. "Automate Everything" is natural evolution of "GeekHOME Server" and most of the old functionality will be moved here sooner or later (and by that I mean a set of plugins for controlling lights, ventilation, central heating, alarm system and others).

# State of the project
The project is still in development.

# Installing on Raspberry Pi
The recommended version of Raspberry Pi is *Raspberry Pi 4*.
The setup below assumes you have a fresh installation of Raspbian/Ubuntu and you can connect to the board with SSH:
```bash
ssh pi@x.x.x.x
```
Where x.x.x.x is the IP address of your Pi in your local network.

### Installing java
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


# Building from sources
"Automate Everything" needs gradle and npm to build. The most usable scripts and gradle tasks are:

* For building plugins:
```
./gradlew :plugins:assemblePlugins
```

* For building frontend (user interface):
```
cd app-server/vuetify
npm install
npm run build
```

* For building backend (the server):
```
./gradlew :app:shadowjar
```

# Directory structure
After building, the directory structure should look like this:
```
app
    |- build
    |   - libs
    |      - app-geekhome-all.jar
    |- plugins
    |   - plugin1.jar
    |   - plugin2.jar
    |   - enabled.txt
    |- vuetify
    |   - dist
    |       - css
    |           - app.*.css
    |           - chunk-vendors.*.css
    |       - js
    |           - app.*.js
    |           - app.*.js.map
    |           - chunk-vendors.*.js
    |           - chunk-vendors.*.js.map
    |       - favicon.ico
    |       - index.html
```

# Running
* To run the server run:
```
cd app-server
java -jar build/libs/app-server-all.jar
```
* or in debug mode:
```
cd app-server
java -Dorg.slf4j.simpleLogger.defaultLogLevel=debug -jar build/libs/app-geekhome-all.jar
```

# Running in slow mode (for UI testing purpose)
When using this option, all requests are going to be delayed for 5 seconds (good for UI testing).
```
cd app-server
java -jar build/libs/app-geekhome-all.jar -slow
```
After starting, open *http://localhost* in your browser