package com.openai.voicenote.ui.screens.voiceRecordScreen

data class VoiceRecordUiState(
    val timerMinutes : String = "00",
    val timerSeconds : String = "00",
    val timerMilliseconds : String = "00",
    val isRecordStarted : Boolean = false,
    val isRecordStopped : Boolean = false,
    val isPlayStarted : Boolean = false,
    val isPlayPaused : Boolean = true,
    val isSpeechToTextConvertStart : Boolean = false
)