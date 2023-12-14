package com.openai.voicenote.ui.screens.noteEditScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.openai.voicenote.data.local.NoteDataSource
import com.openai.voicenote.model.Note
import com.openai.voicenote.utils.QueryDeBouncer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NoteEditViewModel @Inject constructor(private val noteDataSource: NoteDataSource) :
    ViewModel() {

    private val _uiState = MutableStateFlow(NoteEditUiState())
    val uiState: StateFlow<NoteEditUiState> = _uiState.asStateFlow()

    private val noteAutoSaveOrUpdateHandler = QueryDeBouncer<Note>(
        durationInMilliseconds = 500,
        onValue = {
            noteAutoSaveOrUpdate(it)
        }
    )

    private lateinit var currentNote: Note
    private var compose: Boolean = false

    var titleText by mutableStateOf("")
        private set

    var noteText by mutableStateOf("")
        private set

    fun getCompose(): Boolean {
        return compose
    }

    fun setCompose() {
        compose = true
    }

    fun updateTitleText(title: String) {
        titleText = title
        noteAutoSaveOrUpdateHandler.typeTValue = prepareNote()
    }

    fun updateNoteText(note: String) {
        noteText = note
        noteAutoSaveOrUpdateHandler.typeTValue = prepareNote()
    }

    private fun prepareNote(): Note {
        if (::currentNote.isInitialized) {
            currentNote.apply {
                title = titleText
                description = noteText
                editTime = System.currentTimeMillis()
            }
            return currentNote
        }
        currentNote = Note(
            noteId = null,
            title = titleText,
            description = noteText,
            editTime = System.currentTimeMillis(),
            pin = false,
            archive = false,
            backgroundImage = -1
        )
        return currentNote
    }

    private fun noteAutoSaveOrUpdate(note: Note) {
        if (::currentNote.isInitialized) {
            if (currentNote.noteId == null) {
                saveNote()
            } else {
                updateNote()
            }
        }
    }

    fun toggleBottomSheetState(state: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                sheetOpenState = state
            )
        }
    }

    fun changeBackgroundImage(id: Int) {
        if (::currentNote.isInitialized) {
            currentNote.backgroundImage = id
            currentNote.editTime = System.currentTimeMillis()
            noteAutoSaveOrUpdateHandler.typeTValue = currentNote
        }
        else if (id != -1) {
            currentNote = Note(
                noteId = null,
                title = titleText,
                description = noteText,
                editTime = System.currentTimeMillis(),
                pin = false,
                archive = false,
                backgroundImage = id
            )
            noteAutoSaveOrUpdateHandler.typeTValue = currentNote
        }
        _uiState.update { currentState ->
            currentState.copy(
                backgroundImageId = id
            )
        }
    }

    fun setCurrentNote(note: Note) {
        titleText = note.title
        noteText = note.description
        currentNote = note
        noteAutoSaveOrUpdateHandler.typeTValue = currentNote
        _uiState.update { currentState ->
            currentState.copy(
                currentNotePinStatus = note.pin,
                currentNoteArchiveStatus = note.archive,
                backgroundImageId = note.backgroundImage
            )
        }
    }

    private fun saveNote() {
        noteDataSource.insertSingleNote(currentNote) {
            currentNote.noteId = it
        }
    }

    private fun updateNote() {
        noteDataSource.updateNote(currentNote)
    }

    fun togglePinOfNote() {
        if (::currentNote.isInitialized) {
            if (currentNote.noteId != null) {
                currentNote.pin = !currentNote.pin
                noteDataSource.togglePinStatus(currentNote.noteId!!, currentNote.pin)
                _uiState.update { currentState ->
                    currentState.copy(
                        currentNotePinStatus = currentNote.pin
                    )
                }
            }
        }
    }

}