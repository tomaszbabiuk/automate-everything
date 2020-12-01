# Welcome
GeekHOME Server is an multiple solution automation server. GeekHOME can be a base for home automation systems, gardening controllers or even a complicated process controllers. Think of it like more open and plugin robust OpenHab or HoumeAssistant.
It's like bricks to automate whatever you need.

# Configuration
Enabling logs:
```
-Dorg.slf4j.simpleLogger.defaultLogLevel=debug
```

# Building
The system can be build with gradle (backend) and npm (frontend). The most usable tasks are:

* For building plugins
```
./gradlew :plugins:assemblePlugins
```

* Building user interface
```
/build_frontend.sh
```

* Putting everything together
```
./gradlew :app-geekhome:shadowjar
```