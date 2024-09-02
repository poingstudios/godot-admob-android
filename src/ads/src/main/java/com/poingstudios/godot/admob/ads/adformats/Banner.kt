// MIT License
//
// Copyright (c) 2023-present Poing Studios
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
import android.graphics.Rect
import android.os.Build
import android.view.DisplayCutout
import android.view.Gravity
import android.view.View
import android.view.View.OnLayoutChangeListener
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsets
import android.widget.FrameLayout
import com.google.android.gms.ads.*
import com.poingstudios.godot.admob.ads.converters.convertToAdSize
import com.poingstudios.godot.admob.ads.converters.convertToGodotDictionary
import com.poingstudios.godot.admob.core.utils.LogUtils
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
    private var safeArea = getSafeArea()
    private val adPosition: Int = adViewDictionary["ad_position"] as Int
    private lateinit var mAdView: AdView
    private lateinit var mAdSize: AdSize
    private var isHidden : Boolean = false

    private val mLayoutChangeListener =
        OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            LogUtils.debug("OnLayoutChanged")
            val newSafeArea = getSafeArea()
            if (newSafeArea == safeArea){
                return@OnLayoutChangeListener
            }
            safeArea = newSafeArea
            LogUtils.debug("safeArea changed")
            if (!isHidden) { //only update if is not hidden to improve performance
                updatePosition()
            }
        }


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
        val onAdFailedToLoad = SignalInfo("on_ad_failed_to_load", Integer::class.java, Dictionary::class.java)
        val onAdImpression = SignalInfo("on_ad_impression", Integer::class.java)
        val onAdLoaded = SignalInfo("on_ad_loaded", Integer::class.java)
        val onAdOpened = SignalInfo("on_ad_opened", Integer::class.java)
    }


    init {
        val adSizeDictionary = adViewDictionary["ad_size"] as Dictionary
        val adUnitId = adViewDictionary["ad_unit_id"] as String
        val adSize = adSizeDictionary.convertToAdSize()
        activity.runOnUiThread {
            mAdView = AdView(activity)
            mAdView.setAdSize(adSize)
            mAdSize = mAdView.adSize!!
            mAdView.adUnitId = adUnitId
            val layoutParams = getLayoutParams()

            godotLayout.addView(mAdView, layoutParams)

            mAdView.adListener = object : AdListener() {
                override fun onAdClicked() {
                    emitSignal(godot, pluginName, SignalInfos.onAdClicked, UID)
                }

                override fun onAdClosed() {
                    emitSignal(godot, pluginName, SignalInfos.onAdClosed, UID)
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    val loadAdErrorDictionary = loadAdError.convertToGodotDictionary()
                    emitSignal(godot, pluginName, SignalInfos.onAdFailedToLoad, UID, loadAdErrorDictionary)
                }

                override fun onAdImpression() {
                    emitSignal(godot, pluginName, SignalInfos.onAdImpression, UID)
                }

                override fun onAdLoaded() {
                    if (!isHidden) {
                        show()
                    }
                    emitSignal(godot, pluginName, SignalInfos.onAdLoaded, UID)
                }

                override fun onAdOpened() {
                    emitSignal(godot, pluginName, SignalInfos.onAdOpened, UID)
                }
            }
            activity.window.decorView.rootView.addOnLayoutChangeListener(mLayoutChangeListener)
        }
    }

    private fun getSafeArea(): Rect {
        val safeInsetRect = Rect()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            return safeInsetRect
        }
        val window: Window = activity.window?: return safeInsetRect

        if (!isImmersiveModeEnabledLegacy(window)) {
            return safeInsetRect
        }

        val windowInsets : WindowInsets= window.decorView.rootWindowInsets ?: return safeInsetRect
        val displayCutout : DisplayCutout = windowInsets.displayCutout ?: return safeInsetRect

        safeInsetRect.left = displayCutout.safeInsetLeft
        safeInsetRect.top = displayCutout.safeInsetTop
        safeInsetRect.right = displayCutout.safeInsetRight
        safeInsetRect.bottom = displayCutout.safeInsetBottom

        return safeInsetRect
    }

    @Suppress("DEPRECATION")
    private fun isImmersiveModeEnabledLegacy(window: Window): Boolean {
        val uiOptions = window.decorView.systemUiVisibility
        return (uiOptions and View.SYSTEM_UI_FLAG_FULLSCREEN != 0) &&
                (uiOptions and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION != 0) &&
                (uiOptions and View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY != 0)
    }

    private fun getGravity(adPosition: Int?) : Int{
        val gravity = when (adPosition) {
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

        return gravity
    }
    private fun getLayoutParams() : FrameLayout.LayoutParams {
        LogUtils.debug("Safe Area of screen: $safeArea.")

        val adParams : FrameLayout.LayoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT
        )

        adParams.gravity = getGravity(adPosition)
        adParams.bottomMargin = safeArea.bottom
        adParams.rightMargin = safeArea.right
        adParams.leftMargin = safeArea.left
        adParams.topMargin = calculateTopMargin(adParams.topMargin)

        return adParams
    }

    private fun calculateTopMargin(topMargin : Int) : Int{
        var returnValue = topMargin
        when (adPosition) {
            AdPosition.TOP.ordinal, AdPosition.TOP_LEFT.ordinal, AdPosition.TOP_RIGHT.ordinal -> {
                returnValue = safeArea.top
            }
        }
        return returnValue
    }


    private fun updatePosition(){
        activity.runOnUiThread{
            val layoutParams = getLayoutParams()
            mAdView.layoutParams = layoutParams
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
        activity.window.decorView.rootView.removeOnLayoutChangeListener(mLayoutChangeListener)
    }

    fun hide(){
        activity.runOnUiThread{
            isHidden = true
            mAdView.visibility = View.GONE
            mAdView.pause()
        }
    }

    fun show(){
        activity.runOnUiThread{
            isHidden = false
            mAdView.visibility = View.VISIBLE
            updatePosition()
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
