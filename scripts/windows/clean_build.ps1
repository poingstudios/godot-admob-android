Write-Host "Executing Clean Build"

if ($args.Length -eq 0) {
    Write-Host "Error: Please provide the Godot version as an argument."
    exit 1
}

$CURRENT_GODOT_VERSION = $args[0]

Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\scripts\unix\download_godot.sh $CURRENT_GODOT_VERSION
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Undefined

.\gradlew clean
.\gradlew build

$BUILD_EXIT_CODE = $LASTEXITCODE

if ($BUILD_EXIT_CODE -ne 0) {
    Write-Host "Error: ./gradlew build failed with exit code $BUILD_EXIT_CODE"
    exit 1
}

.\gradlew zipPlugins -PgodotVersion=$CURRENT_GODOT_VERSION
