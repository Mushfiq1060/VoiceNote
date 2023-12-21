package com.openai.voicenote.ui.screens.noteEditScreen

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.openai.voicenote.R
import com.openai.voicenote.utils.Utils

data class NoteEditUiState(
    val currentNotePinStatus: Boolean = false,
    val currentNoteArchiveStatus: Boolean = false,
    val sheetOpenState: Boolean = false,
    val backgroundImageId: Int = R.drawable.no_image_24,
    val backgroundColor: Int = Color.Transparent.toArgb(),
    val editedTime: String = Utils.getFormattedTime(System.currentTimeMillis()),
    val isNoteEditStarted: Boolean = false,
    val isUndoPossible: Boolean = false,
    val isRedoPossible: Boolean = false
)