package com.poingstudios.godot.admob.ads

import android.app.Activity
import android.util.ArraySet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.google.android.gms.ads.AdRequest
import com.poingstudios.godot.admob.ads.adformats.Banner
import com.poingstudios.godot.admob.core.AdNetworkExtras
import com.poingstudios.godot.admob.core.utils.LogUtils
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

    override fun onMainResume() {
        super.onMainResume()
        LogUtils.debug("onMainResume")
    }

    override fun onMainPause() {
        super.onMainPause()
        LogUtils.debug("onMainPause")
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
    fun load_ad(uid : Int, adRequestDictionary : Dictionary, keywords : Array<String>){
        LogUtils.debug("Loading Ad!, AdRequestDictionary: $adRequestDictionary, KeyWords: ${keywords.joinToString(", ")}")

        val adRequestBuilder = AdRequest.Builder()
        val mediationExtras = adRequestDictionary["mediation_extras"] as Dictionary
        for ((key) in mediationExtras) {
            val extra = mediationExtras[key] as Dictionary
            val className = extra["class_name"] as String
            val objectClass = Class.forName(className).getDeclaredConstructor().newInstance() as AdNetworkExtras
            val extras = extra["extras"] as Dictionary

            val bundle = objectClass.buildExtras(extras.toMap())
            if (bundle != null){
                adRequestBuilder.addNetworkExtrasBundle(objectClass.getAdapterClass(), bundle)
            }
            else{
                LogUtils.debug("bundle is null: $className")
            }
        }
        for (keyword in keywords) {
           adRequestBuilder.addKeyword(keyword)
        }

        val banner = banners[uid]
        banner?.loadAd(adRequestBuilder.build())
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

