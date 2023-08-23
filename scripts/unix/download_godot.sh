#!/bin/bash
if [ $# -lt 1 ]; then
  echo "Error: Please provide the Godot version as an argument."
  exit 1
fi

CURRENT_GODOT_VERSION="$1"

if [ -z "$2" ]; then
  BUILD_VERSION="stable"
else
  BUILD_VERSION="$2"
fi

cd libs/godot-lib

GODOT_AAR_LIB="godot-lib.aar"
GODOT_AAR_FILENAME="godot-lib.${CURRENT_GODOT_VERSION}.${BUILD_VERSION}"
FULL_PATHNAME_DOWNLOAD_GODOT_AAR="https://downloads.tuxfamily.org/godotengine/${CURRENT_GODOT_VERSION}"

if [ "$BUILD_VERSION" != "stable" ]; then
  FULL_PATHNAME_DOWNLOAD_GODOT_AAR+="/$BUILD_VERSION"
fi

GODOT_AAR_FILENAME+=".template_release.aar"
FULL_PATHNAME_DOWNLOAD_GODOT_AAR+="/$GODOT_AAR_FILENAME"

HTTP_STATUS=$(curl -o "$GODOT_AAR_FILENAME" -w "%{http_code}" "$FULL_PATHNAME_DOWNLOAD_GODOT_AAR")

if [ "$HTTP_STATUS" -eq 200 ]; then
  if [ -f "$GODOT_AAR_LIB" ]; then
    rm "$GODOT_AAR_LIB"
  fi
  mv "$GODOT_AAR_FILENAME" "$GODOT_AAR_LIB"
else
  echo "Error: curl failed with HTTP status $HTTP_STATUS maybe you put a invalid version"
  rm "$GODOT_AAR_FILENAME"
  exit 1
fi