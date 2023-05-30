package com.poingstudios.godot.admob.ads.adformats

import android.app.Activity
import android.widget.FrameLayout
import org.godotengine.godot.Godot

abstract class AdFormatsBase(val UID: Int, val activity: Activity, val godotLayout: FrameLayout, val godot: Godot, val pluginName: String)