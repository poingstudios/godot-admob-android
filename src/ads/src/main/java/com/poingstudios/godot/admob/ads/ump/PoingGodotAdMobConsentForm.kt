package com.poingstudios.godot.admob.ads.ump

import android.app.Activity
import com.google.android.ump.ConsentForm
import com.google.android.ump.UserMessagingPlatform
import com.poingstudios.godot.admob.ads.converters.convertToGodotDictionary
import com.poingstudios.godot.admob.core.utils.LogUtils
import org.godotengine.godot.Dictionary
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin.emitSignal
import org.godotengine.godot.plugin.SignalInfo

class PoingGodotAdMobConsentForm(val UID: Int,
                                 private val consentForm: ConsentForm,
                                 private val activity: Activity,
                                 private val godot: Godot,
                                 private val pluginName: String) {

    object SignalInfos {
        val onConsentFormDismissed = SignalInfo("on_consent_form_dismissed", Integer::class.java, Dictionary::class.java)
    }

    fun show(){
        activity.runOnUiThread {
            consentForm.show(activity)
            {

                LogUtils.debug("consentStatus: ${UserMessagingPlatform.getConsentInformation(activity).consentStatus}")
                emitSignal(godot, pluginName, SignalInfos.onConsentFormDismissed, UID, it?.convertToGodotDictionary()?: Dictionary())
            }
        }
    }
}