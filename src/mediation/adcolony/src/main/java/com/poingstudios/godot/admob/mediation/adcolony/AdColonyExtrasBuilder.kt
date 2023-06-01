package com.poingstudios.godot.admob.mediation.adcolony

import android.os.Bundle
import android.util.Log
import com.google.android.gms.ads.mediation.MediationExtrasReceiver
import com.jirbo.adcolony.AdColonyAdapter
import com.jirbo.adcolony.AdColonyBundleBuilder
import com.poingstudios.godot.admob.core.AdNetworkExtras
import com.poingstudios.godot.admob.core.utils.LogUtils.Companion.LOG_TAG_NAME
import kotlin.String


class AdColonyExtrasBuilder : AdNetworkExtras {
    private val SHOW_PRE_POPUP_KEY = "SHOW_PRE_POPUP_KEY"
    private val SHOW_POST_POPUP_KEY = "SHOW_POST_POPUP_KEY"

    override fun buildExtras(extras: Map<String, Any>?): Bundle {
        val showPrePopup = extras?.get(SHOW_PRE_POPUP_KEY)

        if (showPrePopup != null) {
            AdColonyBundleBuilder.setShowPrePopup(showPrePopup as Boolean)
        }
        val showPostPopup = extras?.get(SHOW_POST_POPUP_KEY)

        if (showPostPopup != null) {
            AdColonyBundleBuilder.setShowPostPopup(showPostPopup as Boolean)
        }
        Log.d(LOG_TAG_NAME, "buildExtras of class : ${getAdapterClass()}")
        return AdColonyBundleBuilder.build()
    }

    override fun getAdapterClass(): Class<out MediationExtrasReceiver> {
        return AdColonyAdapter::class.java
    }
}