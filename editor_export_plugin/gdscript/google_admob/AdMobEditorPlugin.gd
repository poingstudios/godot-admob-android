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
extends EditorPlugin

# Required
var ads_config : AdMobAdsConfig

# Optional - Mediation Support
#var ad_colony_config : AdMobAdColonyConfig
#var meta_config : AdMobMetaConfig
#var vungle_config : AdMobVungleConfig

func _enter_tree():
    ads_config = AndroidExportPlugin.new()
    add_export_plugin(ads_config)

    #ad_colony_config = AndroidExportPlugin.new()
    #add_export_plugin(ad_colony_config)

    #meta_config = AndroidExportPlugin.new()
    #add_export_plugin(meta_config)

    #vungle_config = AndroidExportPlugin.new()
    #add_export_plugin(vungle_config)    

func _exit_tree():
    remove_export_plugin(ads_config)
    ads_config = null

    #remove_export_plugin(ad_colony_config)
    #ad_colony_config = null

    #remove_export_plugin(meta_config)
    #meta_config = null

    #remove_export_plugin(vungle_config)
    #vungle_config = null