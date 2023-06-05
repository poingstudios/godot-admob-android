// MIT License
//
// Copyright (c) 2023 Poing Studios
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

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
    private lateinit var aGodotLayout : FrameLayout

    override fun getPluginName(): String {
        return this::class.simpleName.toString()
    }

    override fun onMainCreate(activity: Activity?): View {
        aGodotLayout = FrameLayout(getActivity()!!)
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
        val banner = Banner(banners.size, activity!!, aGodotLayout, godot, pluginName, adViewDictionary).apply {
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

