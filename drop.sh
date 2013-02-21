#!/bin/bash
set -e
mkdir -p out
rm -rf out/src out/src.zip
cp -r src out/src
find out/src -name .svn |xargs rm -r
cd out
zip -r src src
mv src.zip ~/Dropbox/devel/mapgrid-tms.zip
cd ..
rm -r out
