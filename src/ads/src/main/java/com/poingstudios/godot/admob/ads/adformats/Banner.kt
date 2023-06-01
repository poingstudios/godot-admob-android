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

package com.poingstudios.godot.admob.ads.adformats

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.gms.ads.*
import org.godotengine.godot.Dictionary
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin.emitSignal
import org.godotengine.godot.plugin.SignalInfo


class Banner(
    UID: Int,
    activity: Activity,
    godotLayout: FrameLayout,
    godot: Godot,
    pluginName: String,
    adViewDictionary: Dictionary
) : AdFormatsBase(UID, activity, godotLayout, godot, pluginName) {
    private lateinit var mAdView: AdView
    private lateinit var mAdSize: AdSize

    enum class AdPosition {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT,
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        CENTER
    }
    object SignalInfos {
        val onAdClicked = SignalInfo("on_ad_clicked", Integer::class.java)
        val onAdClosed = SignalInfo("on_ad_closed", Integer::class.java)
        val onAdFailedToLoad = SignalInfo("on_ad_failed_to_load", Integer::class.java)
        val onAdImpression = SignalInfo("on_ad_impression", Integer::class.java)
        val onAdLoaded = SignalInfo("on_ad_loaded", Integer::class.java)
        val onAdOpened = SignalInfo("on_ad_opened", Integer::class.java)
    }

    init {
        activity.runOnUiThread {
            val adSize = adViewDictionary["ad_size"] as Dictionary
            val adUnitId = adViewDictionary["ad_unit_id"] as String

            mAdView = AdView(activity)
            mAdView.setAdSize(AdSize(adSize["width"] as Int, adSize["height"] as Int))
            mAdSize = mAdView.adSize!!
            mAdView.adUnitId = adUnitId
            val adParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT
            )

            val gravity = when (adViewDictionary["ad_position"] as Int) {
                AdPosition.TOP.ordinal -> Gravity.TOP or Gravity.CENTER_HORIZONTAL
                AdPosition.BOTTOM.ordinal -> Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                AdPosition.LEFT.ordinal -> Gravity.START or Gravity.CENTER_VERTICAL
                AdPosition.RIGHT.ordinal -> Gravity.END or Gravity.CENTER_VERTICAL
                AdPosition.TOP_LEFT.ordinal -> Gravity.TOP or Gravity.START
                AdPosition.TOP_RIGHT.ordinal -> Gravity.TOP or Gravity.END
                AdPosition.BOTTOM_LEFT.ordinal -> Gravity.BOTTOM or Gravity.START
                AdPosition.BOTTOM_RIGHT.ordinal -> Gravity.BOTTOM or Gravity.END
                AdPosition.CENTER.ordinal -> Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
                else -> throw IllegalArgumentException("Value of ad_position invalid")
            }
            adParams.gravity = gravity

            godotLayout.addView(mAdView, adParams)

            mAdView.adListener = object : AdListener() {
                override fun onAdClicked() {
                    emitSignal(godot, pluginName, SignalInfos.onAdClicked, UID)
                }

                override fun onAdClosed() {
                    emitSignal(godot, pluginName, SignalInfos.onAdClosed, UID)
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    emitSignal(godot, pluginName, SignalInfos.onAdFailedToLoad, UID)
                }

                override fun onAdImpression() {
                    emitSignal(godot, pluginName, SignalInfos.onAdImpression, UID)
                }

                override fun onAdLoaded() {
                    emitSignal(godot, pluginName, SignalInfos.onAdLoaded, UID)
                }

                override fun onAdOpened() {
                    emitSignal(godot, pluginName, SignalInfos.onAdOpened, UID)
                }
            }
        }
    }


    fun loadAd(adRequest: AdRequest){
        activity.runOnUiThread {
            mAdView.loadAd(adRequest)
        }
    }
    fun destroy(){
        activity.runOnUiThread {
            mAdView.destroy()
            if (mAdView.parent != null){
                (mAdView.parent as ViewGroup).removeView(mAdView)
            }
        }
    }

    fun hide(){
        activity.runOnUiThread{
            mAdView.visibility = View.GONE
            mAdView.pause()
        }
    }

    fun show(){
        activity.runOnUiThread{
            mAdView.visibility = View.VISIBLE
            mAdView.resume()
        }
    }

    fun getWidth() : Int {
        return mAdSize.width
    }

    fun getHeight() : Int{
        return mAdSize.height
    }

    fun getWidthInPixels() : Int{
        return mAdSize.getWidthInPixels(activity)
    }

    fun getHeightInPixels() : Int {
        return mAdSize.getHeightInPixels(activity)
    }

}
