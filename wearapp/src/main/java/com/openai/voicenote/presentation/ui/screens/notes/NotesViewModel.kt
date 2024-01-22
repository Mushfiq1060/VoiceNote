package com.openai.voicenote.presentation.ui.screens.notes

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openai.voicenote.presentation.dataLayer.DataLayer
import com.openai.voicenote.presentation.model.NoteResource
import com.openai.voicenote.presentation.ui.screens.notes.navigation.NOTE_FETCH_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface NotesUiState {

    data object Loading: NotesUiState

    data class Success(
        val notesType: String,
        val noteList: List<NoteResource>
    ): NotesUiState

}

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dataLayer: DataLayer
) : ViewModel() {

    private val notesType = MutableStateFlow("")

    init {
        val noteFetchType = savedStateHandle.get<String>(NOTE_FETCH_TYPE)
        if (noteFetchType != null) {
            notesType.update { noteFetchType }
            viewModelScope.launch {
                dataLayer.sendMessageToHandHeldDevice(noteFetchType)
            }
        }
    }

    val uiState: StateFlow<NotesUiState> = combine(
        notesType.asStateFlow(),
        dataLayer.getNotes(),
        dataLayer.getLoadingStatus()
    ) { type, list, loading ->
        val notesType = when (type) {
            "PIN_NOTES" -> "Pinned Notes"
            "OTHER_NOTES" -> "Other Notes"
            "ARCHIVE_NOTES" -> "Archive Notes"
            "TRASH_NOTES" -> "Trash Notes"
            else -> ""
        }
        if (loading) {
            NotesUiState.Loading
        }
        else {
            NotesUiState.Success(
                notesType = notesType,
                noteList = list
            )
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(100L),
            initialValue = NotesUiState.Loading
        )

}