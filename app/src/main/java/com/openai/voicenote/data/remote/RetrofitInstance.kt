package com.openai.voicenote.data.remote

import com.google.gson.GsonBuilder
import com.openai.voicenote.utils.Constant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
            .build()
    }
    val api: ApiEndPoint by lazy {
        retrofit.create(ApiEndPoint::class.java)
    }
}