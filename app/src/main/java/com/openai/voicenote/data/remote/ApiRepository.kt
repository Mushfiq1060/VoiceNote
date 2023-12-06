package com.openai.voicenote.data.remote

import com.openai.voicenote.model.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class ApiRepository {

    suspend fun transcribeAudio(file : MultipartBody.Part, model : RequestBody, key : String) : Response<ApiResponse> {
        return RetrofitInstance.api.getTextFromAudio(file, model, key)
    }

}