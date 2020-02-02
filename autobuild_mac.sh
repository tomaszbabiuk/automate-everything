#!/bin/sh

pkill -f geekhome
./build_backend.sh
echo "Starting JVM with server in screen session, listening on port 8000"
cd dist
screen -d -m java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000 -jar *.jar
cd ..
echo "Now geekhome will autobuild on modifications of *.java files"

fswatch -r . -e ".*" -i "\\.java$" |
while read path; do
  echo "File modification detected: $path"
  pkill -f geekhome
  ./build_backend.sh
  echo "Starting JVM with server in screen session, listening on port 8000"
  cd dist
  screen -d -m java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000 -jar *.jar
  cd ..
done
