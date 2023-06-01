package com.poingstudios.godot.admob.mediation.vungle

import android.os.Bundle
import android.util.Log
import com.poingstudios.godot.admob.core.AdNetworkExtras
import com.poingstudios.godot.admob.core.utils.LogUtils
import com.vungle.mediation.VungleExtrasBuilder
import kotlin.Any
import kotlin.String

abstract class VunglePoingExtrasBuilder : AdNetworkExtras {
    private val ALL_PLACEMENTS_KEY = "ALL_PLACEMENTS_KEY"
    private val USER_ID_KEY = "USER_ID_KEY"
    private val SOUND_ENABLED_KEY = "SOUND_ENABLED_KEY"

    override fun buildExtras(extras: Map<String, Any>?): Bundle? {
        val placements = extras?.get(ALL_PLACEMENTS_KEY) ?: return null

        val extrasBuilder = VungleExtrasBuilder((placements as String).split(",").toTypedArray())

        val soundEnabled = extras[SOUND_ENABLED_KEY]
        if (soundEnabled != null) {
            extrasBuilder.setStartMuted(soundEnabled as Boolean)
        }

        val userId = extras[USER_ID_KEY]
        if (userId != null) {
            extrasBuilder.setUserId(userId as String)
        }

        return extrasBuilder.build()
    }
}