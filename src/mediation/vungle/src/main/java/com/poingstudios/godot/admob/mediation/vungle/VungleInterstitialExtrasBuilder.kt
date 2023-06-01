package com.poingstudios.godot.admob.mediation.vungle

import com.google.android.gms.ads.mediation.MediationExtrasReceiver
import com.vungle.mediation.VungleInterstitialAdapter

class VungleInterstitialExtrasBuilder : VunglePoingExtrasBuilder() {
    override fun getAdapterClass(): Class<out MediationExtrasReceiver> {
        return VungleInterstitialAdapter::class.java

    }
}