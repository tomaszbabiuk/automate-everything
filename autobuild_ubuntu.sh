#!/bin/sh

sh ./build_backend.sh

inotifywait -r -m ./app/src/main/java -e create |
while read path action file; do
  echo "File modification detected: $path, $action, $file"
  pkill -f geekhome
  sh ./build_backend.sh
  java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000 -jar ./dist/*.jar
done
