package com.openai.voicenote.ui.screens.homeScreen

import com.openai.voicenote.model.Note

data class HomeUiState(
    var isGridEnable : Boolean = true,
    var allPinNotes : List<Note> = listOf(),
    var allOtherNotes : List<Note> = listOf(),
    var selectedPinNotes : MutableSet<Int> = mutableSetOf(),
    var selectedOtherNotes : MutableSet<Int> = mutableSetOf()
)