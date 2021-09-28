# Welcome
"Automate Everything" is a multiple purpose automation server. It can be a base for home automation systems, gardening controllers or even a complicated process controllers. Think of it like more plugin robust OpenHab or deeply customizable HomeAssistant.

# State of the project
The predecessor of "Automate Everything" was called "GeekHOME Server" had been created 10 years ago. This work is in progress!
After UI related coding is done, all the functionality of geekHOME will be moved here.

# Building
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
