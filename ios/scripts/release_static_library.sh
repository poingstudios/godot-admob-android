#!/bin/bash

PLUGIN="admob"

# Compile Plugin
./scripts/generate_static_library.sh $PLUGIN release $1
./scripts/generate_static_library.sh $PLUGIN release_debug $1
mv ./bin/${PLUGIN}.release_debug.a ./bin/${PLUGIN}.debug.a

# Move to release folder
rm -rf ./bin/release
mkdir ./bin/release
rm -rf ./bin/release/${PLUGIN}/admob/bin
mkdir -p ./bin/release/${PLUGIN}/admob/bin
rm -rf ./bin/release/${PLUGIN}/admob/lib
mkdir -p ./bin/release/${PLUGIN}/admob/lib

# Move Plugin
mv ./bin/${PLUGIN}.{release,debug}.a ./bin/release/${PLUGIN}/admob/bin
cp ./plugin/${PLUGIN}/config/${PLUGIN}.gdip ./bin/release/${PLUGIN}

touch ./bin/release/${PLUGIN}/admob/lib/PUT_HERE_ADMOB_SDK
