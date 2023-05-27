package com.poingstudios.godot.admob.ads

import android.app.Activity
import android.util.ArraySet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.AdapterStatus
import com.google.android.gms.ads.initialization.InitializationStatus
import org.godotengine.godot.Dictionary
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.SignalInfo
import org.godotengine.godot.plugin.UsedByGodot

class PoingGodotAdMob(godot: Godot?) : org.godotengine.godot.plugin.GodotPlugin(godot) {
    private lateinit var aActivity: Activity
    private lateinit var aGodotLayout : FrameLayout

    override fun getPluginName(): String {
        return this::class.simpleName.toString()
    }

    override fun onMainCreate(activity: Activity?): View {
        aActivity = super.getActivity()!!
        aGodotLayout = FrameLayout(aActivity)
        return aGodotLayout
    }

    override fun getPluginSignals(): MutableSet<SignalInfo> {
        val signals: MutableSet<SignalInfo> = ArraySet()
        signals.add(SignalInfo("initialization_complete", Dictionary::class.java))
        return signals
    }

    @UsedByGodot
    fun initialize() {
        MobileAds.initialize(aActivity) { initializationStatus ->
            val initializationStatusDictionary = initializationStatus.convertToGodotDictionary();
            emitSignal("initialization_complete", initializationStatusDictionary)
        }
    }

    @UsedByGodot
    fun get_initialization_status(): Dictionary {
        return MobileAds.getInitializationStatus()!!.convertToGodotDictionary()
    }

}