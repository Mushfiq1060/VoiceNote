package com.openai.voicenote.ui.screens.homeScreen

import com.openai.voicenote.model.Note
import com.openai.voicenote.ui.component.MultiFabState

data class HomeUiState(
    var isGridEnable : Boolean = true,
    var allPinNotes : List<Note> = listOf(),
    var allOtherNotes : List<Note> = listOf(),
    var selectedPinNotes : MutableSet<Int> = mutableSetOf(),
    var selectedOtherNotes : MutableSet<Int> = mutableSetOf(),
    var isContextMenuOpen : Boolean = false,
    var fabState : MultiFabState = MultiFabState.COLLAPSED
)