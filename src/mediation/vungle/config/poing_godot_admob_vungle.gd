extends EditorExportPlugin

const PLUGIN_NAME := "vungle"
var _dependency_library := ["com.google.ads.mediation:vungle:6.12.1.1"]

func _supports_platform(platform: EditorExportPlatform) -> bool:
	return platform is EditorExportPlatformAndroid

func _get_android_libraries(_platform: EditorExportPlatform, debug: bool) -> PackedStringArray:
	var variant := "debug" if debug else "release"
	var base := "res://addons/admob/android/bin/libs"

	return PackedStringArray([
		"%s/poing-godot-admob-%s-%s.aar" % [base, PLUGIN_NAME, variant]
	])

func _get_android_dependencies(_platform: EditorExportPlatform, _debug: bool) -> PackedStringArray:
	return PackedStringArray(_dependency_library)