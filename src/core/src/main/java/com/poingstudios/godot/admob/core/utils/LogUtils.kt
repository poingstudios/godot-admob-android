package com.poingstudios.godot.admob.core.utils

import android.util.Log

class LogUtils {
    companion object{
        private const val LOG_TAG_NAME = "poing-godot-admob"
        fun debug(message : String){
            Log.d(LOG_TAG_NAME, message)
        }
    }


}