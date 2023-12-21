package com.openai.voicenote.ui.screens.noteEditScreen

import com.openai.voicenote.utils.Utils

data class NoteEditUiState(
    val currentNotePinStatus: Boolean = false,
    val currentNoteArchiveStatus: Boolean = false,
    val sheetOpenState: Boolean = false,
    val backgroundImageId: Int = -1,
    val editedTime: String = Utils.getFormattedTime(System.currentTimeMillis()),
    val isNoteEditStarted: Boolean = false,
    val isUndoPossible: Boolean = false,
    val isRedoPossible: Boolean = false
)