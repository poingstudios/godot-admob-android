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

        val adapterStatusDictionary = statusMap[adapterClass]?.convertToGodotDictionary();
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
