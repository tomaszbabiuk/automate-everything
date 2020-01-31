inotifywait -r -m ./app/src/main/java -e create |
while read path action file; do
  echo "File modification detected: $path, $action, $file"
  ./build_backend.sh
done
