package com.openai.voicenote.ui.screens.noteEditScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.openai.voicenote.data.local.NoteDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NoteEditViewModel @Inject constructor(private val noteDataSource: NoteDataSource) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteEditUiState())
    val uiState : StateFlow<NoteEditUiState> = _uiState.asStateFlow()

    var titleText by mutableStateOf("")
        private set

    var noteText by mutableStateOf("")
        private set

    fun updateTitleText(title : String) {
        this.titleText = title
    }

    fun updateNoteText(note : String) {
        this.noteText = note
    }

}