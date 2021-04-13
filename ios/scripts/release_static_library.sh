#!/bin/bash

GODOT_PLUGINS="admob"

# Compile Plugin
for lib in $GODOT_PLUGINS; do
    ./scripts/generate_static_library.sh $lib release $1
    ./scripts/generate_static_library.sh $lib release_debug $1
    mv ./bin/${lib}.release_debug.a ./bin/${lib}.debug.a
done

# Move to release folder

rm -rf ./bin/release
mkdir ./bin/release

# Move Plugin
for lib in $GODOT_PLUGINS; do
    mkdir ./bin/release/${lib}
    mv ./bin/${lib}.{release,debug}.a ./bin/release/${lib}
    cp ./plugin/${lib}/${lib}.gdip ./bin/release/${lib}
done