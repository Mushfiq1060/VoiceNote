package com.openai.voicenote.app

import com.openai.voicenote.model.Label

data class AppUiState(
    val selectedIndex: Int = 0,
    val selectedLabelIndex: Int = -1,
    val gestureState: Boolean = true,
    val labelList: List<Label> = listOf()
)
