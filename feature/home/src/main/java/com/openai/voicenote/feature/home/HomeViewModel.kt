package com.openai.voicenote.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openai.voicenote.core.data.NoteDataSource
import com.openai.voicenote.core.data.repository.UserDataRepository
import com.openai.voicenote.core.model.NoteResource
import com.openai.voicenote.core.model.NoteView
import com.openai.voicenote.core.ui.component.FABState
import com.openai.voicenote.core.ui.component.NoteFeedUiState
import com.openai.voicenote.core.ui.component.NoteType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noteDataSource: NoteDataSource,
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private val selectedPinNotes = MutableStateFlow(mutableSetOf<Int>())
    private val selectedOtherNotes = MutableStateFlow(mutableSetOf<Int>())

    val isAnyNoteSelected = MutableStateFlow(false)

    val floatingButtonState = MutableStateFlow(FABState.COLLAPSED)

    val noteViewState = userDataRepository.userData.map { it.noteView }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = NoteView.GRID
        )

    val feedState: StateFlow<NoteFeedUiState> = combine(
        noteDataSource.observeAllPinNotes(),
        noteDataSource.observeAllOtherNotes(),
        selectedPinNotes.asStateFlow(),
        selectedOtherNotes.asStateFlow()
    ) { pinNotes, otherNotes, sPinNotes, sOtherNotes ->
        isAnyNoteSelected.update {
            it.apply {
                (sOtherNotes.isNotEmpty() || sPinNotes.isNotEmpty())
            }
        }
        NoteFeedUiState.Success(sPinNotes, sOtherNotes, pinNotes, otherNotes)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = NoteFeedUiState.Loading
        )

    fun toggleFABState(fabState: FABState) {
        floatingButtonState.update { fabState }
    }

    fun toggleNoteView() {
        viewModelScope.launch {
            userDataRepository.toggleNoteView(noteViewState.value)
        }
    }

    fun addSelectedNote(noteType: NoteType, index: Int) {
        if (noteType == NoteType.PINNED) {
            selectedPinNotes.update {
                it.toMutableSet().apply {
                    add(index)
                }
            }
        }
        else if (noteType == NoteType.OTHERS) {
            selectedOtherNotes.update {
                it.toMutableSet().apply {
                    add(index)
                }
            }
        }
    }

    fun removeSelectedNote(noteType: NoteType, index: Int) {
        if (noteType == NoteType.PINNED) {
            selectedPinNotes.update {
                it.toMutableSet().apply {
                    remove(index)
                }
            }
        }
        else if (noteType == NoteType.OTHERS) {
            selectedOtherNotes.update {
                it.toMutableSet().apply {
                    remove(index)
                }
            }
        }
    }

    private fun removeSelectedNotes() {
        selectedPinNotes.update {
            it.toMutableSet().apply {
                clear()
            }
        }
        selectedOtherNotes.update {
            it.toMutableSet().apply {
                clear()
            }
        }
    }

    private fun getSelectedIdList(): List<Long> {
        val list = mutableListOf<Long>()
        when (val feed = feedState.value) {
            is NoteFeedUiState.Success -> {
                selectedPinNotes.value.forEach {
                    list.add(
                        feed.pinnedNoteList[it].noteId!!
                    )
                }
                selectedOtherNotes.value.forEach {
                    list.add(
                        feed.otherNoteList[it].noteId!!
                    )
                }
            }
            else -> {
                // do nothing
            }
        }
        return list
    }

    fun updateNotesPin() {
        viewModelScope.launch {
            val pin: Boolean = selectedOtherNotes.value.isNotEmpty()
            removeSelectedNotes()
            noteDataSource.togglePinStatus(getSelectedIdList(), pin)
        }
    }

    fun deleteNotes() {
        viewModelScope.launch {
            removeSelectedNotes()
            noteDataSource.deleteNotes(getSelectedIdList())
        }
    }

    fun makeCopyOfNote() {
        viewModelScope.launch {
            val note: NoteResource? = getSelectedNotesForCopy()
            if (note != null) {
                removeSelectedNotes()
                noteDataSource.makeCopyOfNote(note)
            }
        }
    }

    private fun getSelectedNotesForCopy(): NoteResource? {
        when (val feed = feedState.value) {
            is NoteFeedUiState.Success -> {
                if (selectedPinNotes.value.isNotEmpty()) {
                    return feed.pinnedNoteList[selectedPinNotes.value.first()]
                }
                else if (selectedOtherNotes.value.isNotEmpty()) {
                    return feed.otherNoteList[selectedOtherNotes.value.first()]
                }
            }
            else -> {
                return null
            }
        }
        return null
    }


}
