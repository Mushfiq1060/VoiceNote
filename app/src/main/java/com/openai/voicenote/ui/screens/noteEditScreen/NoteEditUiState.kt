package com.openai.voicenote.ui.screens.noteEditScreen

data class NoteEditUiState(
    val titleText : String = "",
    val noteText : String = "",
    val isCurrentNotePin : Boolean = false
)