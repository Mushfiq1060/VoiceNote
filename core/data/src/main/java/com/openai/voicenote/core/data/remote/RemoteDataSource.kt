package com.openai.voicenote.core.data.remote

import com.openai.voicenote.core.model.ApiResponse
import java.io.File

interface RemoteDataSource {

    suspend fun transcribeAudio(file: File): ApiResponse

}