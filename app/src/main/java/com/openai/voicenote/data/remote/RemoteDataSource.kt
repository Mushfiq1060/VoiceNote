package com.openai.voicenote.data.remote

import com.openai.voicenote.model.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File

interface RemoteDataSource {

    suspend fun transcribeAudio(file: File): ApiResponse

}