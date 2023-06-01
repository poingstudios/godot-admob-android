package com.poingstudios.godot.admob.mediation.vungle

import com.poingstudios.godot.admob.core.utils.LogUtils
import com.vungle.warren.Vungle
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.UsedByGodot

class PoingGodotAdMobVungle(godot: Godot?) : org.godotengine.godot.plugin.GodotPlugin(godot) {
    override fun getPluginName(): String {
        return this::class.simpleName.toString()
    }

    @UsedByGodot
    fun update_consent_status(status : Int, consentMessageVersion : String) {
        Vungle.updateConsentStatus(enumValues<Vungle.Consent>()[status], consentMessageVersion)
    }

    @UsedByGodot
    fun update_ccpa_status(status : Int) {
        Vungle.updateCCPAStatus(enumValues<Vungle.Consent>()[status])
    }
}