package com.openai.voicenote.utils

import com.google.gson.Gson

object Utils {

    fun <T> T.toJson() : String? {
        return Gson().toJson(this)
    }

    fun <T> String.fromJson(type : Class<T>) : T {
        return Gson().fromJson(this, type)
    }

}