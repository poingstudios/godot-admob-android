param (
    [string]$CURRENT_GODOT_VERSION,
    [string]$BUILD_VERSION = "stable"
)

if (-not $CURRENT_GODOT_VERSION) {
    Write-Host "Error: Please provide the Godot version as an argument."
    exit 1
}

Set-Location -Path "libs/godot-lib"

$GODOT_AAR_LIB = "godot-lib.aar"
$GODOT_AAR_FILENAME = "godot-lib.$CURRENT_GODOT_VERSION.$BUILD_VERSION"
$FULL_PATHNAME_DOWNLOAD_GODOT_AAR = "https://downloads.tuxfamily.org/godotengine/$CURRENT_GODOT_VERSION"

if ($BUILD_VERSION -ne "stable") {
    $FULL_PATHNAME_DOWNLOAD_GODOT_AAR += "/$BUILD_VERSION"
}

$GODOT_AAR_FILENAME += ".template_release.aar"
$FULL_PATHNAME_DOWNLOAD_GODOT_AAR += "/$GODOT_AAR_FILENAME"

Invoke-WebRequest -Uri $FULL_PATHNAME_DOWNLOAD_GODOT_AAR -OutFile $GODOT_AAR_FILENAME

if (Test-Path $GODOT_AAR_FILENAME) {
    if (Test-Path $GODOT_AAR_LIB) {
        Remove-Item -Path $GODOT_AAR_LIB -Force
    }

    Rename-Item -Path $GODOT_AAR_FILENAME -NewName $GODOT_AAR_LIB
    Write-Host "Download successful"
} else {
    Write-Host "Error: Couldn't download. Maybe you put an invalid version"
    Remove-Item -Path $GODOT_AAR_FILENAME
    exit 1
}
