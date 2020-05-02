echo reinstall common module
cd ..\geekhome-server\module-common
call mvn clean install
cd ..\..\geekhome-server2

echo create artifacts using Maven
call mvn clean package -DskipTests

echo create "dist" directory
rmdir dist /s /q
mkdir -p dist\plugins

echo copy plugins to "dist" directory
xcopy app\target\geekhome-server-app-*.zip dist /s /i
xcopy plugins\core\target\*-all.jar dist\plugins\ /s
xcopy plugins\shelly\target\*-all.jar dist\plugins\ /s
xcopy plugins\enabled.txt dist\plugins\ /s
xcopy plugins\disabled.txt dist\plugins\ /s

cd dist

echo unzip app to "dist" directory
jar xf ..\app\target\geekhome-server-app-*.zip
del geekhome-server-app-*.zip

cd ..