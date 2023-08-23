Write-Host "Executing Clean Build"

if ($args.Length -eq 0) {
    Write-Host "Error: Please provide the Godot version as an argument."
    exit 1
}

$CURRENT_GODOT_VERSION = $args[0]

.\scripts\windows\download_godot.ps1 $CURRENT_GODOT_VERSION
cd ../../
.\gradlew clean
.\gradlew build

$BUILD_EXIT_CODE = $LASTEXITCODE

if ($BUILD_EXIT_CODE -ne 0) {
    Write-Host "Error: ./gradlew build failed with exit code $BUILD_EXIT_CODE"
    exit 1
}

.\gradlew zipPlugins -PgodotVersion="$CURRENT_GODOT_VERSION"