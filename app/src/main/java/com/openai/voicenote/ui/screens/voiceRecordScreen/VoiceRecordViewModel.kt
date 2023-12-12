package com.openai.voicenote.ui.screens.voiceRecordScreen

import android.content.Context
import android.os.CountDownTimer
import android.text.format.DateFormat.getTimeFormat
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openai.voicenote.data.remote.ApiRepository
import com.openai.voicenote.utils.player.AudioPlayer
import com.openai.voicenote.utils.recorder.AudioRecorder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class VoiceRecordViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var apiRepository: ApiRepository
    @Inject
    lateinit var audioRecorderImpl: AudioRecorder
    @Inject
    lateinit var audioPlayerImpl: AudioPlayer

    private val _uiState = MutableStateFlow(VoiceRecordUiState())
    val uiState: StateFlow<VoiceRecordUiState> = _uiState.asStateFlow()

    private lateinit var file: File

    private lateinit var timer: Timer
    private var time: Duration = Duration.ZERO

    private fun startTimer() {
        timer = fixedRateTimer(initialDelay = 0L, daemon = true, period = 65L) {
            time = time.plus(65.milliseconds)
            updateTimeStates()
        }
    }

    private fun updateTimeStates() {
        time.toComponents { _, minutes, seconds, nanoseconds ->
            _uiState.update { currentState ->
                currentState.copy(
                    timerMinutes = getTimeFormat(minutes),
                    timerSeconds = getTimeFormat(seconds),
                    timerMilliseconds = getTimeFormat(nanoseconds / 10000000)
                )
            }
        }
    }

    private fun getTimeFormat(timerTime: Int): String {
        if (timerTime < 10) {
            return "0$timerTime"
        }
        return timerTime.toString()
    }

    fun startRecording(context: Context) {
        if (_uiState.value.isRecordStarted) {
            if (!_uiState.value.isRecordStopped) {
                stopRecording()
            }
            return
        }
        _uiState.update { currentState ->
            currentState.copy(
                isRecordStarted = true,
                isPlayPaused = true
            )
        }
        file = File(context.cacheDir, "audio.mp3")
        audioRecorderImpl.startRecording(file) {
            startTimer()
        }
    }

    private fun stopRecording() {
        timer.cancel()
        _uiState.update { currentState ->
            currentState.copy(
                isRecordStopped = true,
                isPlayPaused = true
            )
        }
        audioRecorderImpl.stopRecording()
    }

    fun startPlaying(context: Context) {
        if (!_uiState.value.isPlayPaused) {
            pausePlaying()
            return
        }
        startTimer()
        if (!_uiState.value.isPlayStarted) {
            time = Duration.ZERO
        }
        audioPlayerImpl.startPlayer(file) {
            // This lambda triggers while media player on complete listener occurs
            _uiState.update { currentState ->
                currentState.copy(
                    isPlayStarted = false,
                    isPlayPaused = true
                )
            }
            stopPlaying()
        }
        _uiState.update { currentState ->
            currentState.copy(
                isPlayStarted = true,
                isPlayPaused = false
            )
        }
    }

    private fun pausePlaying() {
        timer.cancel()
        _uiState.update { currentState ->
            currentState.copy(
                isPlayPaused = true
            )
        }
        audioPlayerImpl.pausePlayer()
    }

    private fun stopPlaying() {
        timer.cancel()
        audioPlayerImpl.stopPlayer()
    }

    fun convertSpeechToText(resultCallBack: (text: String, success: Boolean) -> Unit) {
        _uiState.update { currentState ->
            currentState.copy(
                isSpeechToTextConvertStart = true
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            val requestedFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val fileReqBody = MultipartBody.Part.createFormData("file", file.name, requestedFile)
            val modelReqBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), "whisper-1")
            val response = apiRepository.transcribeAudio(
                fileReqBody,
                modelReqBody,
                "Bearer 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"
            )
            if (response.isSuccessful) {
                val x = response.body()?.text
                if (x != null) {
                    withContext(Dispatchers.Main) {
                        resultCallBack(x, true)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        resultCallBack("Open AI can not process your audio!!!", false)
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    resultCallBack("You reached maximum limit!!!", false)
                }
            }
            withContext(Dispatchers.Main) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isSpeechToTextConvertStart = false
                    )
                }
            }
        }
    }

}