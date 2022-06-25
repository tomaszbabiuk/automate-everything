# Setting development environment

## Prerequisites
IntelliJ Idea (Ultimate or Community Edition)
NodeJS

Automate Everything is using SLF4J for logging

## Preparing working folder
You need to clone **automate-everything** repository which is *the core* of everything and the most crucial plugins.

```bash
mkdir workae

git clone https://github.com/tomaszbabiuk/automate-everything-ci.git
git clone https://github.com/tomaszbabiuk/automate-everything.git

git clone https://github.com/tomaszbabiuk/aeplugin-afore
git clone https://github.com/tomaszbabiuk/aeplugin-alarm
git clone https://github.com/tomaszbabiuk/aeplugin-bashaction
git clone https://github.com/tomaszbabiuk/aeplugin-centralheating
git clone https://github.com/tomaszbabiuk/aeplugin-cryptotrading
git clone https://github.com/tomaszbabiuk/aeplugin-emailaction
git clone https://github.com/tomaszbabiuk/aeplugin-homeautomationicons
git clone https://github.com/tomaszbabiuk/aeplugin-mobileaccess
git clone https://github.com/tomaszbabiuk/aeplugin-onewire
git clone https://github.com/tomaszbabiuk/aeplugin-onoff
git clone https://github.com/tomaszbabiuk/aeplugin-scenes
git clone https://github.com/tomaszbabiuk/aeplugin-sensorsandcontrollers
git clone https://github.com/tomaszbabiuk/aeplugin-shelly
git clone https://github.com/tomaszbabiuk/aeplugin-time
git clone https://github.com/tomaszbabiuk/aeplugin-zigbee2mqtt    
```

## Setting up IntelliJ IDEA (backend)

- Open **workae/automate-everything-ci** project.
- Create a new **JAR application** configuration:
  - Path to JAR: (...)/workae/automate-everything/output/bin/ae-backend-all.jar
  - Working directory: (...)/workae/automate-everything/output
  - Before launch
    - Run Gradle **assembleBackend** task (gradle project **(...)/workae/automate-everything/ae-backend**)
    - Run Gradle **buildForAutomateEverything** task (gradle project **(...)/workae/aeplugin-time**)
    - repeat this step for the rest of the plugins

## Setting up the frontend

```bash
cd workae/automate-everything/ae-frontend
npm install
npm run build
```

## Debugging
- Run the frontend in terminal:
```bash
cd workae/automate-everything/ae-frontend
npm run serve
```

- Debug *JAR application* configuration
- Go to http://localhost:8080