extends EditorExportPlugin

const PLUGIN_NAME := "ads"
var _dependency_library := ["com.google.android.gms:play-services-ads:24.9.0"]

func _supports_platform(platform: EditorExportPlatform) -> bool:
	return platform is EditorExportPlatformAndroid

func _get_android_libraries(_platform: EditorExportPlatform, debug: bool) -> PackedStringArray:
	var variant := "debug" if debug else "release"
	var base := "res://addons/admob/android/bin/%s/libs" % PLUGIN_NAME

	return PackedStringArray([
		"%s/poing-godot-admob-%s-%s.aar" % [base, PLUGIN_NAME, variant],
		"%s/poing-godot-admob-core-%s.aar" % [base, variant]
	])

func _get_android_dependencies(_platform: EditorExportPlatform, _debug: bool) -> PackedStringArray:
	return PackedStringArray(_dependency_library)