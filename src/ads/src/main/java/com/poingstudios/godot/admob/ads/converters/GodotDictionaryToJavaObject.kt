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

package com.poingstudios.godot.admob.ads.converters

import android.app.Activity
import com.google.android.gms.ads.AdSize
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentRequestParameters
import com.poingstudios.godot.admob.core.utils.LogUtils
import org.godotengine.godot.Dictionary


fun Dictionary.convertToAdSize(): AdSize {
    return AdSize(get("width") as Int, get("height") as Int)
}

fun Dictionary.convertToConsentDebugSettings(activity: Activity) : ConsentDebugSettings {
    val debugSettingsBuilder = ConsentDebugSettings.Builder(activity)

    debugSettingsBuilder.setDebugGeography(this["debug_geography"] as Int)

    for (value in (this["test_device_hashed_ids"] as Dictionary).values){
        debugSettingsBuilder.addTestDeviceHashedId(value as String)
    }
    return debugSettingsBuilder.build()
}

fun Dictionary.convertToConsentRequestParameters(activity: Activity): ConsentRequestParameters {
    val consentRequestParametersBuilder = ConsentRequestParameters.Builder()
    val tagForUnderAgeOfConsent = this["tag_for_under_age_of_consent"]
    if (tagForUnderAgeOfConsent is Boolean) {
        consentRequestParametersBuilder.setTagForUnderAgeOfConsent(tagForUnderAgeOfConsent)
    }

    val consentDebugSettingsDictionary = this["consent_debug_settings"] as Dictionary?
    val consentDebugSettings = consentDebugSettingsDictionary?.convertToConsentDebugSettings(activity)
    if (consentDebugSettings != null){
        consentRequestParametersBuilder.setConsentDebugSettings(consentDebugSettings)
    }

    return consentRequestParametersBuilder.build()
}

