package com.poingstudios.godot.admob.core

import android.os.Bundle
import com.google.android.gms.ads.mediation.MediationExtrasReceiver

interface AdNetworkExtras {
    fun buildExtras(extras: Map<String, Any>?): Bundle

    fun getAdapterClass(): Class<out MediationExtrasReceiver>
}