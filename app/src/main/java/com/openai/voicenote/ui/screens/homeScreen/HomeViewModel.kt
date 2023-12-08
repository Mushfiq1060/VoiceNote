package com.openai.voicenote.ui.screens.homeScreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
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

}