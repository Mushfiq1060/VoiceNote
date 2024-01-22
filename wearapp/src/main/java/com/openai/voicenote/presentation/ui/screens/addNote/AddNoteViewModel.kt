package com.openai.voicenote.presentation.ui.screens.addNote

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openai.voicenote.core.designsystem.icon.VnColor
import com.openai.voicenote.core.designsystem.icon.VnImage
import com.openai.voicenote.presentation.dataLayer.DataLayer
import com.openai.voicenote.presentation.model.NoteResource
import com.openai.voicenote.presentation.utils.Utils.toJson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val dataLayer: DataLayer
) : ViewModel() {

    val noteText = MutableStateFlow("")

    fun onNoteTextChange(text: String) {
        noteText.update { text }
    }

    fun saveNoteToHandHeldDevice() {
        val note = NoteResource(
            noteId = null,
            title = "",
            description = noteText.value,
            editTime = System.currentTimeMillis(),
            pin = false,
            archive = false,
            backgroundColor = VnColor.bgColorList[0].id,
            backgroundImage = VnImage.bgImageList[0].id
        )
        viewModelScope.launch {
            dataLayer.sendNoteToHandHeldDevice(note)
        }
    }

}