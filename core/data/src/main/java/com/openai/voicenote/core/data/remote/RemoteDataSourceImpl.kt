package com.openai.voicenote.core.data.remote

import android.util.Log
import com.openai.voicenote.core.data.BuildConfig
import com.openai.voicenote.core.data.remote.api.ApiEndPoint
import com.openai.voicenote.core.data.remote.api.ApiRepository
import com.openai.voicenote.core.model.ApiResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSourceImpl @Inject constructor(
    private val apiRepository: ApiRepository,
) : RemoteDataSource {

    override suspend fun transcribeAudio(file: File): ApiResponse {
        val requestedFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val fileReqBody = MultipartBody.Part.createFormData("file", file.name, requestedFile)
        val modelReqBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), "whisper-1")
        val header = "Bearer ${BuildConfig.OPEN_AI_API_KEY}"
        val response = apiRepository.transcribeAudio(fileReqBody, modelReqBody, header)
        if (response.isSuccessful) {
            val text = response.body()?.text
            return if (text != null) {
                ApiResponse(
                    text = text,
                    success = true
                )
            } else {
                ApiResponse(
                    text = "Open AI can not process your request!!!",
                    success = false
                )
            }
        }
        return ApiResponse(
            text = "Please check your API key!!!",
            success = false
        )
    }

}