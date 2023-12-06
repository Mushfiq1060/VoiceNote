package com.openai.voicenote.data.remote

import com.openai.voicenote.model.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRepository @Inject constructor(private val api : ApiEndPoint) {

    suspend fun transcribeAudio(file : MultipartBody.Part, model : RequestBody, key : String) : Response<ApiResponse> {
        return api.getTextFromAudio(file, model, key)
    }

}