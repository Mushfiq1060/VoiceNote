package com.openai.voicenote.ui.screens.noteEditScreen

data class NoteEditUiState(
    val currentNotePinStatus: Boolean = false,
    val currentNoteArchiveStatus: Boolean = false,
    val sheetOpenState: Boolean = false,
    val backgroundImageId: Int = -1,
    val editedTime: String = ""
)