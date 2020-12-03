# Welcome
GeekHOME Server is a multiple purpose automation server. It can be a base for home automation systems, gardening controllers or even a complicated process controllers. Think of it like more plugin robust OpenHab or deeply customizable HomeAssistant.

# State of the project
The first GeekHOME Server had been created 10 years ago. The work on "This version 2.0" is in progress!
After UI related coding is done, all the functionality of geekHOME will be moved here.

# Building
GeekHOME needs gradle and npm to build. The most usable scripts and gradle tasks are:

* For building plugins:
```
./gradlew :plugins:assemblePlugins
```

* For building frontend (user interface):
```
cd app-geekhome/vuetify
npm install
npm run build
```

* For building backend (the server):
```
./gradlew :app-geekhome:shadowjar
```

# Directory structure
After building, the directory structure should look like this:
```
app-geekhome
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
cd app-geekhome
java -jar build/libs/app-geekhome-all.jar
```
* or in debug mode:
```
cd app-geekhome
java -Dorg.slf4j.simpleLogger.defaultLogLevel=debug -jar build/libs/app-geekhome-all.jar
```

After starting, open *http://localhost* in your browser

-Dfile.encoding=UTF-8?