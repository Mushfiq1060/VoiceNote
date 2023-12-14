package com.openai.voicenote.ui.screens.voiceRecordScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openai.voicenote.data.remote.RemoteDataSource
import com.openai.voicenote.data.remote.api.ApiRepository
import com.openai.voicenote.model.ApiResponse
import com.openai.voicenote.utils.player.AudioPlayer
import com.openai.voicenote.utils.player.AudioPlayerImpl
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

@HiltViewModel
class VoiceRecordViewModel @Inject constructor(
    private val remoteDataSourceImpl: RemoteDataSource,
    private val audioRecorderImpl: AudioRecorder,
    private val audioPlayerImpl: AudioPlayerImpl
) : ViewModel() {

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

    fun convertSpeechToText(resultCallBack: (response: ApiResponse) -> Unit) {
        _uiState.update { currentState ->
            currentState.copy(
                isSpeechToTextConvertStart = true
            )
        }
        viewModelScope.launch {
            val result = remoteDataSourceImpl.transcribeAudio(file)
            withContext(Dispatchers.Main) {
                resultCallBack(result)
            }
        }
    }

}