package com.poingstudios.godot.admob.mediation.adcolony

import com.adcolony.sdk.AdColonyAppOptions
import com.google.ads.mediation.adcolony.AdColonyMediationAdapter
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.UsedByGodot

class PoingGodotAdMobAdColonyAppOptions(godot: Godot?) : org.godotengine.godot.plugin.GodotPlugin(godot) {
    private val appOptions: AdColonyAppOptions = AdColonyMediationAdapter.getAppOptions()

    override fun getPluginName(): String {
        return this::class.simpleName.toString()
    }

    @UsedByGodot
    fun set_privacy_framework_required(type : String, required : Boolean){
        appOptions.setPrivacyFrameworkRequired(type, required)
    }
    @UsedByGodot
    fun get_privacy_framework_required(type : String) : Boolean {
        return appOptions.getPrivacyFrameworkRequired(type)
    }
    @UsedByGodot
    fun set_privacy_consent_string(type : String, consentString : String){
        appOptions.setPrivacyConsentString(type, consentString)
    }
    @UsedByGodot
    fun get_privacy_consent_string(type : String) : String {
        return appOptions.getPrivacyConsentString(type)
    }
    @UsedByGodot
    fun set_user_id(userId : String){
        appOptions.userID = userId
    }
    @UsedByGodot
    fun get_user_id() : String{
        return appOptions.userID
    }
    @UsedByGodot
    fun set_test_mode(enabled : Boolean){
        appOptions.testModeEnabled = enabled
    }
    @UsedByGodot
    fun get_test_mode() : Boolean{
        return appOptions.testModeEnabled
    }

}