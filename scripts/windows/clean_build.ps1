Write-Host "Executing Clean Build"

if ($args.Length -eq 0) {
    Write-Host "Error: Please provide the Godot version as an argument."
    exit 1
}

$CURRENT_GODOT_VERSION = $args[0]

.\scripts\windows\download_godot.ps1 $CURRENT_GODOT_VERSION
if ($LASTEXITCODE -eq 1) {
    exit 1
}

cd ../../
.\gradlew clean
.\gradlew build

if ($LASTEXITCODE -eq 0) {
    exit 1
}

.\gradlew zipPlugins -PgodotVersion="$CURRENT_GODOT_VERSION"