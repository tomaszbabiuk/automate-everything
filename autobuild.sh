inotifywait -r -m ./app/src/main/java -e create |
while read path action file; do
  echo "File modification detected: $path, $action, $file"
  ./build_backend.sh
  pkill -f geekhome
  java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000 -jar ./dist/*.jar
done
