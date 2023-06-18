package com.poingstudios.godot.admob.ads

import android.util.ArraySet
import com.google.android.ump.UserMessagingPlatform
import com.poingstudios.godot.admob.ads.converters.convertToGodotDictionary
import com.poingstudios.godot.admob.ads.ump.PoingGodotAdMobConsentForm
import com.poingstudios.godot.admob.core.utils.LogUtils
import org.godotengine.godot.Dictionary
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.SignalInfo
import org.godotengine.godot.plugin.UsedByGodot


class PoingGodotAdMobUserMessagingPlatform(godot: Godot?) : org.godotengine.godot.plugin.GodotPlugin(godot){
    private val consentForms = mutableListOf<PoingGodotAdMobConsentForm?>()

    override fun getPluginName(): String {
        return this::class.simpleName.toString()
    }

    override fun getPluginSignals(): MutableSet<SignalInfo> {
        val signals: MutableSet<SignalInfo> = ArraySet()
        signals.add(SignalInfo("on_consent_form_load_success_listener", Integer::class.java))
        signals.add(SignalInfo("on_consent_form_load_failure_listener", Dictionary::class.java))
        signals.add(PoingGodotAdMobConsentForm.SignalInfos.onConsentFormDismissed)
        return signals
    }

    @UsedByGodot
    fun load_consent_form(){
        activity!!.runOnUiThread {
            LogUtils.debug("load_consent_form")
            UserMessagingPlatform.loadConsentForm(
                activity!!,
                {
                    val consentForm = PoingGodotAdMobConsentForm(consentForms.size, it, activity!!, godot, pluginName).apply {
                        consentForms.add(this)
                    }
                    emitSignal("on_consent_form_load_success_listener", consentForm.UID)
                },
                {
                    emitSignal("on_consent_form_load_failure_listener", it.convertToGodotDictionary())
                }
            )
        }
    }

    @UsedByGodot
    fun show(UID : Int){
        consentForms[UID]?.show()
    }
}