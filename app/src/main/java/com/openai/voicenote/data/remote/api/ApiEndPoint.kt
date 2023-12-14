package com.openai.voicenote.data.remote.api

import com.openai.voicenote.model.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiEndPoint {

    @Multipart
    @POST("audio/transcriptions")
    suspend fun getTextFromAudio(
        @Part file: MultipartBody.Part,
        @Part("model") model: RequestBody,
        @Header("Authorization") apiKey: String
    ): Response<ApiResponse>

}