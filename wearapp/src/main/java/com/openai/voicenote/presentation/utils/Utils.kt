package com.openai.voicenote.presentation.utils

import android.net.Uri
import com.google.gson.Gson

object Utils {

    fun <T> T.toJson(): String {
        val gson = Gson()
        return Uri.encode(gson.toJson(this))
    }

    fun <T> String.fromJson(type: Class<T>): T {
        return Gson().fromJson(this, type)
    }

}