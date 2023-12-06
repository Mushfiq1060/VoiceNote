package com.openai.voicenote.ui.screens.voiceRecordScreen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openai.voicenote.data.remote.ApiRepository
import com.openai.voicenote.utils.player.AudioPlayer
import com.openai.voicenote.utils.player.AudioPlayerImpl
import com.openai.voicenote.utils.recorder.AudioRecorder
import com.openai.voicenote.utils.recorder.AudioRecorderImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class VoiceRecordViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(VoiceRecordUiState())
    val uiState : StateFlow<VoiceRecordUiState> = _uiState.asStateFlow()

    private var apiRepository : ApiRepository = ApiRepository()
    lateinit var audioRecorderImpl : AudioRecorder
    lateinit var audioPlayerImpl : AudioPlayer
    private lateinit var file : File

    fun startRecording(context: Context) {
        if (_uiState.value.isRecordStarted) {
            if (!_uiState.value.isRecordStopped) {
                stopRecording(context = context)
            }
            return
        }
        _uiState.update { currentState ->
            currentState.copy(
                isRecordStarted = true,
                isPlayPaused = true
            )
        }
        audioRecorderImpl  = AudioRecorderImpl(context)
        file = File(context.cacheDir, "audio.mp3")
        audioRecorderImpl.startRecording(file)
    }

    private fun stopRecording(context: Context) {
        _uiState.update { currentState ->
            currentState.copy(
                isRecordStopped = true,
                isPlayPaused = true
            )
        }
        audioRecorderImpl.stopRecording()
//        viewModelScope.launch(Dispatchers.IO) {
//            val requestedFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
//            val fileReqBody = MultipartBody.Part.createFormData("file", file.name, requestedFile)
//            val modelReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), "whisper-1")
//            val response = apiRepository.transcribeAudio(fileReqBody, modelReqBody, "Bearer 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")
//            if (response.isSuccessful) {
//                val x = response.body()?.text
//                if (x != null) {
//                    Log.i("TAGG", x)
//                }
//                else {
//                    Log.i("TAGG", "NOOOOO.......")
//                }
//            }
//            else {
//                Log.i("TAGG", "Unsuccessful + ${response.raw()}")
//            }
//        }
    }

    fun startPlaying(context: Context) {
        if (!_uiState.value.isPlayPaused) {
            pausePlaying()
            return
        }
        if (!this::audioPlayerImpl.isInitialized) {
            audioPlayerImpl = AudioPlayerImpl(context)
        }
        audioPlayerImpl.startPlayer(file) {
            _uiState.update { currentState ->
                currentState.copy(
                    isPlayPaused = true
                )
            }
            stopPlaying()
        }
        _uiState.update { currentState ->
            currentState.copy(
                isPlayPaused = false
            )
        }
    }

    private fun pausePlaying() {
        _uiState.update { currentState ->
            currentState.copy(
                isPlayPaused = true
            )
        }
        audioPlayerImpl.pausePlayer()
    }

    private fun stopPlaying() {
        audioPlayerImpl.stopPlayer()
    }

}