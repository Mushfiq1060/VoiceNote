package com.openai.voicenote.ui.screens.noteEditScreen

import android.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.openai.voicenote.data.local.NoteDataSource
import com.openai.voicenote.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NoteEditViewModel @Inject constructor(private val noteDataSource: NoteDataSource) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteEditUiState())
    val uiState : StateFlow<NoteEditUiState> = _uiState.asStateFlow()

    var titleText by mutableStateOf("")
        private set

    var noteText by mutableStateOf("")
        private set

    var prevNote : Note? =  null

    fun updateTitleText(title : String) {
        this.titleText = title
    }

    fun updateNoteText(note : String) {
        this.noteText = note
    }

    fun setPreviousNote(note : Note) {
        prevNote = note
        _uiState.update { currentState ->
            currentState.copy(
                isCurrentNotePin = note.pin
            )
        }
    }

    fun saveNote() {
        val notes = mutableListOf<Note>()
        notes.add(Note(
            noteId = null,
            title = titleText,
            description = noteText,
            editTime = System.currentTimeMillis(),
            backgroundImage = -1,
            pin = false,
            archive = false
        ))
        noteDataSource.insertNote(notes)
    }

    fun togglePinOfNote() {
        if(prevNote == null) {
            return
        }
        prevNote?.noteId?.let { noteDataSource.togglePinStatus(it, !prevNote?.pin!!) }
        prevNote!!.pin = !prevNote!!.pin
        _uiState.update { currentState ->
            currentState.copy(
                isCurrentNotePin = prevNote!!.pin
            )
        }
    }

}