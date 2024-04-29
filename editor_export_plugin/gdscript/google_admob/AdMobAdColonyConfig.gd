# MIT License
#
# Copyright (c) 2024-present Poing Studios
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.

@tool
extends EditorExportPlugin

var _plugin_name = "PoingGodotAdMobAdColony"

var _lib_path_release = "godot_admob/libs/poing-godot-admob-adcolony-v1.0.0-release.aar"
var _lib_path_debug = "godot_admob/libs/poing-godot-admob-adcolony-v1.0.0-debug.aar"
var _ad_colony_dependency = "com.google.ads.mediation:adcolony:4.8.0.2"

func _supports_platform(platform):
    if (platform is EditorExportPlatformAndroid):
        return true
    else:
        return false

func _get_android_libraries(platform, debug):
    if (debug):
        return PackedStringArray([_lib_path_debug])
    else:
        return PackedStringArray([_lib_path_release])

func _get_android_dependencies(platform, debug):
    return PackedStringArray([_ad_colony_dependency])
    
func _get_name():
    return _plugin_name