package com.openai.voicenote.ui.screens.voiceRecordScreen

import android.content.Context
import android.os.CountDownTimer
import android.text.format.DateFormat.getTimeFormat
import android.util.Log
import androidx.lifecycle.ViewModel
import com.openai.voicenote.data.remote.ApiRepository
import com.openai.voicenote.utils.player.AudioPlayer
import com.openai.voicenote.utils.recorder.AudioRecorder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class VoiceRecordViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var apiRepository : ApiRepository
    @Inject lateinit var audioRecorderImpl : AudioRecorder
    @Inject lateinit var audioPlayerImpl : AudioPlayer

    private val _uiState = MutableStateFlow(VoiceRecordUiState())
    val uiState : StateFlow<VoiceRecordUiState> = _uiState.asStateFlow()

    private lateinit var file : File

    private lateinit var timer : Timer
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

    private fun getTimeFormat(timerTime: Int) : String {
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
//        viewModelScope.launch(Dispatchers.IO) {
//            val requestedFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
//            val fileReqBody = MultipartBody.Part.createFormData("file", file.name, requestedFile)
//            val modelReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), "whisper-1")
//            val response = apiRepository.transcribeAudio(fileReqBody, modelReqBody, "Bearer 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")
////            val response = apiRepository.transcribeAudio(fileReqBody, modelReqBody, "Bearer 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") // previous api-key
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
        startTimer()
        if(!_uiState.value.isPlayStarted) {
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

}