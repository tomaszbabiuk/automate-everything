cd dist

# run app
java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000 -jar geekhome-server-app-1.0.jar
