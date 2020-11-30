#!/bin/sh

cd vuetify
npm run build
rm -rf ../app-geekhome/web
mv -v dist/* ../app-geekhome/web