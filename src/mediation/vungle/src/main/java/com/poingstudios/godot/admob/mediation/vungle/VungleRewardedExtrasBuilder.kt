package com.poingstudios.godot.admob.mediation.vungle

import com.google.android.gms.ads.mediation.MediationExtrasReceiver
import com.vungle.mediation.VungleAdapter

class VungleRewardedExtrasBuilder : VunglePoingExtrasBuilder() {
    override fun getAdapterClass(): Class<out MediationExtrasReceiver> {
        return VungleAdapter::class.java
    }
}