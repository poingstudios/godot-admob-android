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

cd godot-lib

GODOT_AAR_LIB="godot-lib.aar"
GODOT_AAR_FILENAME="godot-lib.${CURRENT_GODOT_VERSION}.${BUILD_VERSION}"
FULL_PATHNAME_DOWNLOAD_GODOT_AAR="https://downloads.tuxfamily.org/godotengine/${CURRENT_GODOT_VERSION}"

if [ "$BUILD_VERSION" != "stable" ]; then
  FULL_PATHNAME_DOWNLOAD_GODOT_AAR+="/$BUILD_VERSION"
fi

GODOT_AAR_FILENAME+=".template_release.aar"
FULL_PATHNAME_DOWNLOAD_GODOT_AAR+="/$GODOT_AAR_FILENAME"

# Baixe o arquivo
curl -O "$FULL_PATHNAME_DOWNLOAD_GODOT_AAR"
mv "$GODOT_AAR_FILENAME" "$GODOT_AAR_LIB"