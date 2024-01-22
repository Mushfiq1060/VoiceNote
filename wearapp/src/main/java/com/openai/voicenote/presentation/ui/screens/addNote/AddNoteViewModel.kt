package com.openai.voicenote.presentation.ui.screens.addNote

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.openai.voicenote.presentation.dataLayer.DataLayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val dataLayer: DataLayer
) : ViewModel() {

    val noteText = MutableStateFlow("")

    fun onNoteTextChange(text: String) {
        noteText.update { text }
    }

}