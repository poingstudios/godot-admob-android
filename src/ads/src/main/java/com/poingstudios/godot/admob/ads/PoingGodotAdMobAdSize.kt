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

import android.util.DisplayMetrics
import android.view.Display
import com.google.android.gms.ads.AdSize
import com.poingstudios.godot.admob.ads.converters.convertToGodotDictionary
import com.poingstudios.godot.admob.core.utils.LogUtils
import org.godotengine.godot.Dictionary
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.UsedByGodot


class PoingGodotAdMobAdSize(godot: Godot?) : org.godotengine.godot.plugin.GodotPlugin(godot)  {

    override fun getPluginName(): String {
        return this::class.simpleName.toString()
    }

    @UsedByGodot
    fun getCurrentOrientationAnchoredAdaptiveBannerAdSize(width : Int) : Dictionary{
        LogUtils.debug("calling getCurrentOrientationAnchoredAdaptiveBannerAdSize")
        val currentWidth = if (width == AdSize.FULL_WIDTH) getAdWidth() else width
        LogUtils.debug("currentWidth: $currentWidth")

        val adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity!!, currentWidth)
        return adSize.convertToGodotDictionary()
    }

    @UsedByGodot
    fun getPortraitAnchoredAdaptiveBannerAdSize(width : Int) : Dictionary{
        LogUtils.debug("calling getPortraitAnchoredAdaptiveBannerAdSize")
        val currentWidth = if (width == AdSize.FULL_WIDTH) getAdWidth() else width
        LogUtils.debug("currentWidth: $currentWidth")

        val adSize = AdSize.getPortraitAnchoredAdaptiveBannerAdSize(activity!!, currentWidth)
        return adSize.convertToGodotDictionary()
    }

    @UsedByGodot
    fun getLandscapeAnchoredAdaptiveBannerAdSize(width : Int) : Dictionary{
        LogUtils.debug("calling getLandscapeAnchoredAdaptiveBannerAdSize")
        val currentWidth = if (width == AdSize.FULL_WIDTH) getAdWidth() else width
        LogUtils.debug("currentWidth: $currentWidth")

        val adSize = AdSize.getLandscapeAnchoredAdaptiveBannerAdSize(activity!!, currentWidth)
        return adSize.convertToGodotDictionary()
    }

    @UsedByGodot
    fun getSmartBannerAdSize() : Dictionary {
        LogUtils.debug("calling getSmartBannerAdSize")

        return AdSize.SMART_BANNER.convertToGodotDictionary()
    }

    private fun getAdWidth(): Int {
        val display: Display = activity!!.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val density = outMetrics.density

        val adWidthPixels = outMetrics.widthPixels.toFloat()

        return (adWidthPixels / density).toInt()
    }


}