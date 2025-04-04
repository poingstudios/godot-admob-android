extends EditorExportPlugin

var _plugin_name := "ads"
var _dependency_library := "com.google.android.gms:play-services-ads:23.3.0"

func _supports_platform(platform: EditorExportPlatform) -> bool:
	if platform is EditorExportPlatformAndroid:
		return true
	return false

func _get_android_libraries(_platform: EditorExportPlatform, debug: bool) -> PackedStringArray:
	if debug:
		return PackedStringArray([
			"admob/bin/android/" + _plugin_name + "/poing-godot-admob-" + _plugin_name + "-debug.aar",
			"admob/bin/android/" + _plugin_name + "/poing-godot-admob-core-debug.aar"
		])
	else:
		return PackedStringArray([
			"admob/bin/android/" + _plugin_name + "/poing-godot-admob-" + _plugin_name + "-release.aar",
			"admob/bin/android/" + _plugin_name + "/poing-godot-admob-core-release.aar"
		])

func _get_android_dependencies(_platform, debug):
	return PackedStringArray([_dependency_library])
