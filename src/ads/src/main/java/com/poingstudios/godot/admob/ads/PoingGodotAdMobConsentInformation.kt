package com.poingstudios.godot.admob.ads

import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation.OnConsentInfoUpdateFailureListener
import com.google.android.ump.ConsentInformation.OnConsentInfoUpdateSuccessListener
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.poingstudios.godot.admob.core.utils.LogUtils
import org.godotengine.godot.Dictionary
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.UsedByGodot


class PoingGodotAdMobConsentInformation(godot: Godot?) : org.godotengine.godot.plugin.GodotPlugin(godot){
    override fun getPluginName(): String {
        return this::class.simpleName.toString()
    }
    @UsedByGodot
    fun update(consentRequestParametersDictionary: Dictionary){
        LogUtils.debug("update PoingGodotAdMobConsentInformation: $consentRequestParametersDictionary")
        var consentForm1: ConsentForm
        val consentInformation = UserMessagingPlatform.getConsentInformation(activity!!)
        val params = ConsentRequestParameters.Builder()
            .setTagForUnderAgeOfConsent(false)
            .build()
        consentInformation.requestConsentInfoUpdate(
            activity!!,
            params,
            {
                LogUtils.debug("requestConsentInfoUpdate")
                // The consent information state was updated.
                // You are now ready to check if a form is available.
                UserMessagingPlatform.loadConsentForm(
                    activity!!,
                    {
                        LogUtils.debug("loadConsentForm")
                        consentForm1 = it
                        consentForm1.show(activity!!) {
                            LogUtils.debug("showFormError ${it?.message}")
                        }
                    },
                    {
                        LogUtils.debug("loadConsentFormError")
                    }
                )
            },
            {
                LogUtils.debug("requestConsentInfoUpdateError")
                // Handle the error.
            }
        )

    }
}