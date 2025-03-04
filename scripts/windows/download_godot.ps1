param (
    [string]$CURRENT_GODOT_VERSION,
    [string]$BUILD_VERSION = "stable"
)

if (-not $CURRENT_GODOT_VERSION) {
    Write-Host "Error: Please provide the Godot version as an argument."
    exit 1
}

$dir = "libs/godot-lib"
if (-not (Test-Path $dir)) {
    New-Item -Path $dir -ItemType Directory -Force | Out-Null
}

Set-Location -Path $dir

$GODOT_AAR_LIB = "godot-lib.aar"
$GODOT_AAR_FILENAME = "godot-lib.$CURRENT_GODOT_VERSION.$BUILD_VERSION.template_release.aar"
$FULL_PATHNAME_DOWNLOAD_GODOT_AAR = "https://github.com/godotengine/godot-builds/releases/download/$CURRENT_GODOT_VERSION-$BUILD_VERSION/$GODOT_AAR_FILENAME"

Write-Host "FULL_PATHNAME_DOWNLOAD_GODOT_AAR: $FULL_PATHNAME_DOWNLOAD_GODOT_AAR"

try {
    $response = Invoke-WebRequest -Uri $FULL_PATHNAME_DOWNLOAD_GODOT_AAR -OutFile $GODOT_AAR_FILENAME -PassThru
    $HTTP_STATUS = $response.StatusCode
} catch {
    $HTTP_STATUS = $_.Exception.Response.StatusCode.Value__
}

if ($HTTP_STATUS -eq 200) {
    if (Test-Path $GODOT_AAR_LIB) {
        Remove-Item -Path $GODOT_AAR_LIB -Force
    }
    Rename-Item -Path $GODOT_AAR_FILENAME -NewName $GODOT_AAR_LIB
    Write-Host "Download successful"
} else {
    Write-Host "Error: Invoke-WebRequest failed with HTTP status $HTTP_STATUS maybe you put an invalid version"
    if (Test-Path $GODOT_AAR_FILENAME) {
        Remove-Item -Path $GODOT_AAR_FILENAME -Force
    }
    exit 1
}