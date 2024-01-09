package com.openai.voicenote.feature.recordaudio

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openai.voicenote.core.common.utils.player.AudioPlayerImpl
import com.openai.voicenote.core.common.utils.recorder.AudioRecorderImpl
import com.openai.voicenote.core.data.remote.RemoteDataSource
import com.openai.voicenote.core.data.repository.FileRepository
import com.openai.voicenote.core.model.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

data class RecordAudioUiState(
    val isRecordStarted: Boolean = false,
    val isRecordStopped: Boolean = false,
    val isPlayStarted: Boolean = false,
    val isPlayPaused: Boolean = false,
    val isSpeechToTextConvertStart: Boolean = false
)

@HiltViewModel
class RecordAudioViewModel @Inject constructor(
    private val audioRecorderImpl: AudioRecorderImpl,
    private val audioPlayerImpl: AudioPlayerImpl,
    private val fileRepository: FileRepository,
    private val remoteDataSource: RemoteDataSource
) : ViewModel() {

    private val mUiState = MutableStateFlow(RecordAudioUiState())
    val uiState: StateFlow<RecordAudioUiState> = mUiState.asStateFlow()

    val timerState = MutableStateFlow("00:00.00")

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
            timerState.update {
                getTimeFormat(minutes) + ":" + getTimeFormat(seconds) +
                        "." + getTimeFormat(nanoseconds / 10000000)
            }
        }
    }

    private fun getTimeFormat(timerTime: Int): String {
        if (timerTime < 10) {
            return "0$timerTime"
        }
        return timerTime.toString()
    }

    fun startRecording() {
        if (mUiState.value.isRecordStarted) {
            if (!mUiState.value.isRecordStopped) {
                stopRecording()
            }
            return
        }
        mUiState.update { currentState ->
            currentState.copy(
                isRecordStarted = true,
                isPlayPaused = true
            )
        }
        file = fileRepository.createFile("audio.mp3")
        audioRecorderImpl.startRecording(file) {
            startTimer()
        }
    }

    private fun stopRecording() {
        timer.cancel()
        mUiState.update { currentState ->
            currentState.copy(
                isRecordStopped = true,
                isPlayPaused = true
            )
        }
        audioRecorderImpl.stopRecording()
    }

    fun startPlaying() {
        if (!mUiState.value.isPlayPaused) {
            pausePlaying()
            return
        }
        startTimer()
        if (!mUiState.value.isPlayStarted) {
            time = Duration.ZERO
        }
        audioPlayerImpl.startPlayer(file) {
            /** This lambda triggers while media
             * player on complete listener occurs**/
            mUiState.update { currentState ->
                currentState.copy(
                    isPlayStarted = false,
                    isPlayPaused = true
                )
            }
            stopPlaying()
        }
        mUiState.update { currentState ->
            currentState.copy(
                isPlayStarted = true,
                isPlayPaused = false
            )
        }
    }

    private fun pausePlaying() {
        timer.cancel()
        mUiState.update { currentState ->
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

    fun convertSpeechToText(resultCallback: (response: ApiResponse) -> Unit) {
        mUiState.update { currentState ->
            currentState.copy(
                isSpeechToTextConvertStart = true
            )
        }
        viewModelScope.launch {
            val result = remoteDataSource.transcribeAudio(file)
            withContext(Dispatchers.Main) {
                resultCallback(result)
            }
        }
    }

}