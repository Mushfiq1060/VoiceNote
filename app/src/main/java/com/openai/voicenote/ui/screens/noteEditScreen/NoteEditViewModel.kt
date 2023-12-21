package com.openai.voicenote.ui.screens.noteEditScreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openai.voicenote.R
import com.openai.voicenote.data.local.NoteDataSource
import com.openai.voicenote.model.Note
import com.openai.voicenote.utils.QueryDeBouncer
import com.openai.voicenote.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteEditViewModel @Inject constructor(private val noteDataSource: NoteDataSource) :
    ViewModel() {

    private val _uiState = MutableStateFlow(NoteEditUiState())
    val uiState: StateFlow<NoteEditUiState> = _uiState.asStateFlow()

    private val history =
        mutableListOf<Pair<String, String>>() // first one is for note title & second one is for note description
    private val redoHistory =
        mutableListOf<Pair<String, String>>() // first one is for note title & second one is for note description
    private var isAddedToHistory = true

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

    fun updateTitleText(title: String, isAddedToHistory: Boolean = true) {
        if (!_uiState.value.isNoteEditStarted) {
            _uiState.update { currentState ->
                currentState.copy(
                    isNoteEditStarted = true
                )
            }
        }
        this.isAddedToHistory = isAddedToHistory
        titleText = title
        noteAutoSaveOrUpdateHandler.typeTValue = prepareNote()
    }

    fun updateNoteText(note: String, isAddedToHistory: Boolean = true) {
        if (!_uiState.value.isNoteEditStarted) {
            _uiState.update { currentState ->
                currentState.copy(
                    isNoteEditStarted = true
                )
            }
        }
        this.isAddedToHistory = isAddedToHistory
        noteText = note
        noteAutoSaveOrUpdateHandler.typeTValue = prepareNote()
    }

    fun updateSpeechToText(note: String) {
        isAddedToHistory = true
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
            backgroundImage = R.drawable.no_image_24,
            backgroundColor = Color.Transparent.toArgb()
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
            currentNote.backgroundColor = Color.Transparent.toArgb()
            currentNote.editTime = System.currentTimeMillis()
            noteAutoSaveOrUpdateHandler.typeTValue = currentNote
        } else {
            currentNote = Note(
                noteId = null,
                title = titleText,
                description = noteText,
                editTime = System.currentTimeMillis(),
                pin = false,
                archive = false,
                backgroundImage = id,
                backgroundColor = Color.Transparent.toArgb()
            )
            noteAutoSaveOrUpdateHandler.typeTValue = currentNote
        }
        _uiState.update { currentState ->
            currentState.copy(
                backgroundImageId = id,
                backgroundColor = Color.Transparent.toArgb()
            )
        }
    }

    fun changeBackgroundColor(color: Int) {
        if (::currentNote.isInitialized) {
            currentNote.backgroundImage = R.drawable.no_image_24
            currentNote.backgroundColor = color
            currentNote.editTime = System.currentTimeMillis()
            noteAutoSaveOrUpdateHandler.typeTValue = currentNote
        } else {
            currentNote = Note(
                noteId = null,
                title = titleText,
                description = noteText,
                editTime = System.currentTimeMillis(),
                pin = false,
                archive = false,
                backgroundImage = R.drawable.no_image_24,
                backgroundColor = color
            )
            noteAutoSaveOrUpdateHandler.typeTValue = currentNote
        }
        _uiState.update { currentState ->
            currentState.copy(
                backgroundImageId = R.drawable.no_image_24,
                backgroundColor = color
            )
        }
    }

    fun setCurrentNote(note: Note) {
        titleText = note.title
        noteText = note.description
        currentNote = note
        _uiState.update { currentState ->
            currentState.copy(
                currentNotePinStatus = note.pin,
                currentNoteArchiveStatus = note.archive,
                backgroundImageId = note.backgroundImage,
                backgroundColor = note.backgroundColor,
                editedTime = Utils.getFormattedTime(note.editTime)
            )
        }
    }

    private fun toggleUndoRedoState() {
        _uiState.update { currentState ->
            currentState.copy(
                isUndoPossible = history.size > 1,
                isRedoPossible = redoHistory.isNotEmpty()
            )
        }
    }

    fun addHistory(title: String, desc: String) {
        history.add(Pair(title, desc))
        redoHistory.clear()
        toggleUndoRedoState()
    }

    fun undoHistory() {
        if (history.size > 1) {
            val lastHistory = history.removeLast()
            redoHistory.add(lastHistory)
            toggleUndoRedoState()
            updateTitleText(history.last().first, false)
            updateNoteText(history.last().second, false)
        }
    }

    fun redoHistory() {
        if (redoHistory.isNotEmpty()) {
            val lastRedoHistory = redoHistory.removeLast()
            history.add(lastRedoHistory)
            toggleUndoRedoState()
            updateTitleText(lastRedoHistory.first, false)
            updateNoteText(lastRedoHistory.second, false)
        }
    }

    private fun saveNote() {
        viewModelScope.launch {
            currentNote.noteId = noteDataSource.insertSingleNote(currentNote)
            if (_uiState.value.isNoteEditStarted && isAddedToHistory) {
                addHistory(currentNote.title, currentNote.description)
            }
            _uiState.update { currentState ->
                currentState.copy(
                    editedTime = Utils.getFormattedTime(currentNote.editTime)
                )
            }
        }
    }

    private fun updateNote() {
        viewModelScope.launch {
            noteDataSource.updateNote(currentNote)
            if (_uiState.value.isNoteEditStarted && isAddedToHistory) {
                addHistory(currentNote.title, currentNote.description)
            }
            _uiState.update { currentState ->
                currentState.copy(
                    editedTime = Utils.getFormattedTime(currentNote.editTime)
                )
            }
        }
    }

    fun togglePinOfNote() {
        if (::currentNote.isInitialized) {
            if (currentNote.noteId != null) {
                currentNote.pin = !currentNote.pin
                viewModelScope.launch {
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

}