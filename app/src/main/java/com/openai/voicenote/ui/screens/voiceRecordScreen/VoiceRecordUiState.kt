package com.openai.voicenote.ui.screens.voiceRecordScreen

data class VoiceRecordUiState(
    val isRecordStarted : Boolean = false,
    val isRecordStopped : Boolean = false,
    val isPlayPaused : Boolean = true
)