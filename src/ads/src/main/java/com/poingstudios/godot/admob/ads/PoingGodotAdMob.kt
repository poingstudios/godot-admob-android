package com.poingstudios.godot.admob.ads

import android.app.Activity
import android.util.ArraySet
import android.view.View
import android.widget.FrameLayout
import com.google.android.gms.ads.*
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
            val initializationStatusDictionary = initializationStatus.convertToGodotDictionary()
            emitSignal("initialization_complete", initializationStatusDictionary)
        }
    }

    @UsedByGodot
    fun set_request_configuration(requestConfigurationDictionary: Dictionary, testDeviceIds : Array<String>) {
        val maxAdContentRating = requestConfigurationDictionary["max_ad_content_rating"] as String
        val tagForChildDirectedTreatment = requestConfigurationDictionary["tag_for_child_directed_treatment"] as Int
        val tagForUnderAgeOfConsent = requestConfigurationDictionary["tag_for_under_age_of_consent"] as Int

        val requestConfiguration = RequestConfiguration.Builder()
            .setMaxAdContentRating(maxAdContentRating)
            .setTagForChildDirectedTreatment(tagForChildDirectedTreatment)
            .setTagForUnderAgeOfConsent(tagForUnderAgeOfConsent)
            .setTestDeviceIds(testDeviceIds.toList())
            .build()

        MobileAds.setRequestConfiguration(requestConfiguration)
    }

    @UsedByGodot
    fun get_initialization_status(): Dictionary {
        return MobileAds.getInitializationStatus()!!.convertToGodotDictionary()
    }



}