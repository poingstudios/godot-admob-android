package com.poingstudios.godot.admob.ads

import android.app.Activity
import android.util.ArraySet
import android.view.View
import android.widget.FrameLayout
import com.poingstudios.godot.admob.ads.adformats.Banner
import org.godotengine.godot.Dictionary
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.SignalInfo
import org.godotengine.godot.plugin.UsedByGodot

class PoingGodotAdMobAdView(godot: Godot?) : org.godotengine.godot.plugin.GodotPlugin(godot)  {
    private lateinit var aActivity: Activity
    private lateinit var aGodotLayout : FrameLayout

    override fun getPluginName(): String {
        return this::class.simpleName.toString()
    }

    override fun onMainCreate(activity: Activity?): View {
        aActivity = super.getActivity()!!
        aGodotLayout = FrameLayout(aActivity)
        return aGodotLayout
    }

    private val banners = mutableListOf<Banner?>()


    override fun getPluginSignals(): MutableSet<SignalInfo> {
        val signals: MutableSet<SignalInfo> = ArraySet()
        signals.add(Banner.SignalInfos.onAdClicked)
        signals.add(Banner.SignalInfos.onAdClosed)
        signals.add(Banner.SignalInfos.onAdFailedToLoad)
        signals.add(Banner.SignalInfos.onAdImpression)
        signals.add(Banner.SignalInfos.onAdLoaded)
        signals.add(Banner.SignalInfos.onAdOpened)
        return signals
    }

    @UsedByGodot
    fun create(adViewDictionary : Dictionary) : Int{
        val banner = Banner(banners.size, aActivity, aGodotLayout, godot, pluginName, adViewDictionary).apply {
            banners.add(this)
        }

        return banner.UID
    }

    @UsedByGodot
    fun load_ad(uid : Int){
        val banner = banners[uid]
        banner?.loadAd()
    }
    @UsedByGodot
    fun destroy(uid : Int){
        banners[uid]?.destroy()
        banners[uid] = null
    }

    @UsedByGodot
    fun hide(uid : Int){
        banners[uid]?.hide()
    }

    @UsedByGodot
    fun show(uid : Int){
        banners[uid]?.show()
    }

    @UsedByGodot
    fun get_width(uid : Int) : Int{
        return banners[uid]?.getWidth() ?: -1
    }

    @UsedByGodot
    fun get_height(uid : Int) : Int {
        return banners[uid]?.getHeight() ?: -1
    }

    @UsedByGodot
    fun get_width_in_pixels(uid : Int) : Int {
        return banners[uid]?.getWidthInPixels() ?: -1
    }

    @UsedByGodot
    fun get_height_in_pixels(uid : Int) : Int {
        return banners[uid]?.getHeightInPixels() ?: -1
    }

}

