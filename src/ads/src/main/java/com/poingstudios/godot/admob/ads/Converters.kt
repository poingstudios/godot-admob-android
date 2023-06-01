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

import android.util.Log
import com.google.android.gms.ads.initialization.AdapterStatus
import com.google.android.gms.ads.initialization.InitializationStatus
import org.godotengine.godot.Dictionary

fun InitializationStatus.convertToGodotDictionary() : Dictionary{
    val statusMap = adapterStatusMap
    val initializationStatusDictionary = Dictionary()

    for (adapterClass in statusMap.keys) {
        Log.d("poing-godot-admob", "")

        val adapterStatusDictionary = statusMap[adapterClass]?.convertToGodotDictionary()
        initializationStatusDictionary[adapterClass] = adapterStatusDictionary
    }
    return initializationStatusDictionary
}

fun AdapterStatus.convertToGodotDictionary() : Dictionary {
    val adapterStatusDictionary = Dictionary()
    adapterStatusDictionary["latency"] = latency
    adapterStatusDictionary["initializationState"] = initializationState.ordinal
    adapterStatusDictionary["description"] = description

    return adapterStatusDictionary
}
