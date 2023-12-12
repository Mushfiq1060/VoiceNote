package com.openai.voicenote.ui.screens.homeScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.openai.voicenote.data.local.NoteDataSource
import com.openai.voicenote.model.Note
import com.openai.voicenote.ui.component.MultiFabState
import com.openai.voicenote.utils.NoteType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val noteDataSource: NoteDataSource) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        getAllPinNotes()
        getAllOtherNotes()
    }

    private fun getAllPinNotes() {
        noteDataSource.getAllPinNotes {
            _uiState.update { currentState ->
                currentState.copy(
                    allPinNotes = it
                )
            }
        }
    }

    private fun getAllOtherNotes() {
        noteDataSource.getAllOtherNotes {
            _uiState.update { currentState ->
                currentState.copy(
                    allOtherNotes = it
                )
            }
        }
    }

    fun toggleFabState(fabState: MultiFabState) {
        _uiState.update { currentState ->
            currentState.copy(
                fabState = fabState
            )
        }
    }

    fun toggleGridView() {
        _uiState.update { currentState ->
            currentState.copy(
                isGridEnable = !currentState.isGridEnable
            )
        }
    }

    fun toggleContextMenuState() {
        _uiState.update { currentState ->
            currentState.copy(
                isContextMenuOpen = !currentState.isContextMenuOpen
            )
        }
    }

    fun addSelectedNotes(type: NoteType, index: Int) {
        if (type == NoteType.PIN) {
            _uiState.update { currentState ->
                currentState.copy(
                    selectedPinNotes = currentState.selectedPinNotes.toMutableSet()
                        .apply { add(index) },
                )
            }
        } else if (type == NoteType.Other) {
            _uiState.update { currentState ->
                currentState.copy(
                    selectedOtherNotes = currentState.selectedOtherNotes.toMutableSet()
                        .apply { add(index) },
                )
            }
        }
    }

    fun removeSelectedNotes() {
        _uiState.update { currentState ->
            currentState.copy(
                selectedPinNotes = currentState.selectedPinNotes.toMutableSet().apply { clear() },
                selectedOtherNotes = currentState.selectedOtherNotes.toMutableSet()
                    .apply { clear() }
            )
        }
    }

    fun checkNoteIsSelected(type: NoteType, index: Int): Boolean {
        if (type == NoteType.PIN) {
            return _uiState.value.selectedPinNotes.contains(index)
        } else if (type == NoteType.Other) {
            return _uiState.value.selectedOtherNotes.contains(index)
        }
        return false
    }

    fun removeSelectedNote(type: NoteType, index: Int) {
        if (type == NoteType.PIN) {
            _uiState.update { currentState ->
                currentState.copy(
                    selectedPinNotes = currentState.selectedPinNotes.toMutableSet()
                        .apply { remove(index) }
                )
            }
        } else if (type == NoteType.Other) {
            _uiState.update { currentState ->
                currentState.copy(
                    selectedOtherNotes = currentState.selectedOtherNotes.toMutableSet()
                        .apply { remove(index) }
                )
            }
        }
    }

    fun makeCopyOfNote() {
        var note: Note? = null
        if (_uiState.value.selectedPinNotes.isNotEmpty()) {
            note = _uiState.value.allPinNotes[_uiState.value.selectedPinNotes.first()].copy(
                noteId = null,
                editTime = System.currentTimeMillis()
            )
        } else if (_uiState.value.selectedOtherNotes.isNotEmpty()) {
            note = _uiState.value.allOtherNotes[_uiState.value.selectedOtherNotes.first()].copy(
                noteId = null,
                editTime = System.currentTimeMillis()
            )
        }
        if (note != null) {
            noteDataSource.insertSingleNote(note) {
                note.noteId = it
                _uiState.update { currentState ->
                    currentState.copy(
                        allOtherNotes = currentState.allOtherNotes.toMutableList()
                            .apply { add(0, note) }
                    )
                }
                removeSelectedNotes()
            }
        }
    }

    fun deleteNotes() {
        val deletedIdList = mutableListOf<Long>()
        _uiState.value.selectedPinNotes.forEach {
            _uiState.value.allPinNotes[it].noteId?.let { id -> deletedIdList.add(id) }
        }
        _uiState.value.selectedOtherNotes.forEach {
            _uiState.value.allOtherNotes[it].noteId?.let { id -> deletedIdList.add(id) }
        }
        noteDataSource.deleteNotes(deletedIdList) {
            removeSelectedNotes()
            getAllOtherNotes()
            getAllPinNotes()
        }
    }

    fun updateNotesPin() {
        val updateIdList = mutableListOf<Long>()
        var pin: Boolean = false
        if (_uiState.value.selectedOtherNotes.isNotEmpty()) {
            _uiState.value.selectedPinNotes.forEach {
                _uiState.value.allPinNotes[it].noteId?.let { id -> updateIdList.add(id) }
            }
            _uiState.value.selectedOtherNotes.forEach {
                _uiState.value.allOtherNotes[it].noteId?.let { id -> updateIdList.add(id) }
            }
            pin = true
        } else if (_uiState.value.selectedPinNotes.isNotEmpty()) {
            _uiState.value.selectedPinNotes.forEach {
                _uiState.value.allPinNotes[it].noteId?.let { id -> updateIdList.add(id) }
            }
            pin = false
        }
        noteDataSource.updatePinStatus(updateIdList, pin) {
            removeSelectedNotes()
            getAllOtherNotes()
            getAllPinNotes()
        }
    }
}