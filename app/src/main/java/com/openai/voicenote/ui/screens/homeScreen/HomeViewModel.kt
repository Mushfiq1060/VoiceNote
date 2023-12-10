package com.openai.voicenote.ui.screens.homeScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.openai.voicenote.data.local.NoteDataSource
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
    val uiState : StateFlow<HomeUiState> = _uiState.asStateFlow()

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

    fun toggleGridView() {
        _uiState.update { currentState ->
            currentState.copy(
                isGridEnable = !currentState.isGridEnable
            )
        }
    }

    fun addSelectedNotes(type : NoteType, index : Int) {
        if (type == NoteType.PIN) {
            _uiState.update { currentState ->
                currentState.copy(
                    selectedPinNotes = currentState.selectedPinNotes.toMutableSet().apply { add(index) },
                )
            }
        }
        else if (type == NoteType.Other) {
            _uiState.update { currentState ->
                currentState.copy(
                    selectedOtherNotes = currentState.selectedOtherNotes.toMutableSet().apply { add(index) },
                )
            }
        }
    }

    fun removeSelectedNotes() {
        _uiState.update { currentState ->
            currentState.copy(
                selectedPinNotes = currentState.selectedPinNotes.toMutableSet().apply { clear() },
                selectedOtherNotes = currentState.selectedOtherNotes.toMutableSet().apply { clear() }
            )
        }
    }

    fun checkNoteIsSelected(type : NoteType, index : Int) : Boolean {
        if (type == NoteType.PIN) {
            return _uiState.value.selectedPinNotes.contains(index)
        }
        else if (type == NoteType.Other) {
            return _uiState.value.selectedOtherNotes.contains(index)
        }
        return false
    }

    fun removeSelectedNotes(type : NoteType, index : Int) {
        if (type == NoteType.PIN) {
            _uiState.update { currentState ->
                currentState.copy(
                    selectedPinNotes = currentState.selectedPinNotes.toMutableSet().apply { remove(index) }
                )
            }
        }
        else if (type == NoteType.Other) {
            _uiState.update { currentState ->
                currentState.copy(
                    selectedOtherNotes = currentState.selectedOtherNotes.toMutableSet().apply { remove(index) }
                )
            }
        }
    }

}