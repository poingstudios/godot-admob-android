#!/bin/bash
echo "Executing Clean Build"

if [ $# -eq 0 ]; then
  echo "Error: Please provide the Godot version as an argument."
  exit 1
fi

CURRENT_GODOT_VERSION="$1"

chmod +x ./scripts/unix/download_godot.sh
./scripts/unix/download_godot.sh "${CURRENT_GODOT_VERSION}"
./gradlew build
./gradlew zipPlugins -PgodotVersion="${CURRENT_GODOT_VERSION}"