package com.openai.voicenote.core.data.remote.api

import com.openai.voicenote.core.model.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRepository @Inject constructor(private val api: ApiEndPoint) {

    suspend fun transcribeAudio(
        file: MultipartBody.Part,
        model: RequestBody,
        key: String
    ): Response<ApiResponse> {
        return api.getTextFromAudio(file, model, key)
    }

}