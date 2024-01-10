package com.openai.voicenote.feature.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openai.voicenote.core.data.local.NoteDataSource
import com.openai.voicenote.core.data.local.repository.UserDataRepository
import com.openai.voicenote.core.designsystem.icon.VnColor
import com.openai.voicenote.core.designsystem.icon.VnImage
import com.openai.voicenote.core.model.NoteResource
import com.openai.voicenote.core.model.NoteView
import com.openai.voicenote.core.ui.component.FABState
import com.openai.voicenote.core.ui.component.NoteFeedUiState
import com.openai.voicenote.core.ui.component.NoteType
import com.openai.voicenote.core.ui.component.SelectedTopAppBarItem
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
class NotesViewModel @Inject constructor(
    private val noteDataSource: NoteDataSource,
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    private val selectedPinNotes = MutableStateFlow(mutableSetOf<Long>())
    private val selectedOtherNotes = MutableStateFlow(mutableSetOf<Long>())

    val isAnyNoteSelected = MutableStateFlow(false)
    val contextMenuState = MutableStateFlow(false)

    val floatingButtonState = MutableStateFlow(FABState.COLLAPSED)

    val noteViewState = userDataRepository.userData.map { it.noteView }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = NoteView.GRID
        )

    val feedState: StateFlow<NoteFeedUiState> = combine(
        noteDataSource.observeAllPinNotesWithLabels(),
        noteDataSource.observeAllOtherNotesWithLabels(),
        selectedPinNotes.asStateFlow(),
        selectedOtherNotes.asStateFlow()
    ) { pinNotes, otherNotes, sPinNotes, sOtherNotes ->
        isAnyNoteSelected.update {
            (sOtherNotes.isNotEmpty() || sPinNotes.isNotEmpty())
        }
        contextMenuState.update {
            !(sOtherNotes.isNotEmpty() || sPinNotes.isNotEmpty())
        }
        NoteFeedUiState.Success(sPinNotes, sOtherNotes, pinNotes, otherNotes)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = NoteFeedUiState.Loading
        )

    fun getEmptyNote(): NoteResource {
        return NoteResource(
            noteId = null,
            title = "",
            description = "",
            editTime = 0L,
            pin = false,
            archive = false,
            backgroundColor = VnColor.bgColorList[0].id,
            backgroundImage = VnImage.bgImageList[0].id
        )
    }

    fun onSelectedTopAppBarClick(item: SelectedTopAppBarItem) {
        when (item) {
            SelectedTopAppBarItem.CANCEL -> { removeAllSelectedNotes() }
            SelectedTopAppBarItem.TOGGLE_PIN -> { updateNotesPin() }
            SelectedTopAppBarItem.DRAW -> {

            }
            SelectedTopAppBarItem.LABEL -> { removeAllSelectedNotes() }
            SelectedTopAppBarItem.CONTEXT_MENU_OPEN -> { contextMenuState.update { true } }
            SelectedTopAppBarItem.CONTEXT_MENU_CLOSE -> { removeAllSelectedNotes() }
            SelectedTopAppBarItem.TOGGLE_ARCHIVE -> { updateNotesArchive() }
            SelectedTopAppBarItem.DELETE -> { deleteNotes() }
            SelectedTopAppBarItem.MAKE_A_COPY -> { makeCopyOfNote() }
        }
    }

    fun toggleFABState(fabState: FABState) {
        floatingButtonState.update { fabState }
    }

    fun toggleNoteView() {
        viewModelScope.launch {
            userDataRepository.toggleNoteView(noteViewState.value)
        }
    }

    fun checkSelectedNote(type: NoteType, noteId: Long) {
        if (type == NoteType.PINNED) {
            if (selectedPinNotes.value.contains(noteId)) {
                removeSelectedNote(type, noteId)
            } else {
                addSelectedNote(type, noteId)
            }
        }
        else if (type == NoteType.OTHERS) {
            if (selectedOtherNotes.value.contains(noteId)) {
                removeSelectedNote(type, noteId)
            } else {
                addSelectedNote(type, noteId)
            }
        }
    }

    private fun addSelectedNote(noteType: NoteType, noteId: Long) {
        if (noteType == NoteType.PINNED) {
            selectedPinNotes.update {
                it.toMutableSet().apply {
                    add(noteId)
                }
            }
        }
        else if (noteType == NoteType.OTHERS) {
            selectedOtherNotes.update {
                it.toMutableSet().apply {
                    add(noteId)
                }
            }
        }
    }

    private fun removeSelectedNote(noteType: NoteType, noteId: Long) {
        if (noteType == NoteType.PINNED) {
            selectedPinNotes.update {
                it.toMutableSet().apply {
                    remove(noteId)
                }
            }
        }
        else if (noteType == NoteType.OTHERS) {
            selectedOtherNotes.update {
                it.toMutableSet().apply {
                    remove(noteId)
                }
            }
        }
    }

    private fun removeAllSelectedNotes() {
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
        selectedPinNotes.value.forEach {
            list.add(it)
        }
        selectedOtherNotes.value.forEach {
            list.add(it)
        }
        return list
    }

    private fun updateNotesArchive() {
        viewModelScope.launch {
            noteDataSource.toggleArchiveStatus(getSelectedIdList(), true)
            removeAllSelectedNotes()
        }
    }

    private fun updateNotesPin() {
        viewModelScope.launch {
            val pin: Boolean = selectedOtherNotes.value.isNotEmpty()
            noteDataSource.togglePinStatus(getSelectedIdList(), pin)
            removeAllSelectedNotes()
        }
    }

    private fun deleteNotes() {
        viewModelScope.launch {
            noteDataSource.deleteNotes(getSelectedIdList())
            removeAllSelectedNotes()
        }
    }

    private fun makeCopyOfNote() {
        viewModelScope.launch {
            noteDataSource.makeCopyOfNote(getSelectedNotesIdForCopy())
            removeAllSelectedNotes()
        }
    }

    private fun getSelectedNotesIdForCopy(): Long {
        if (selectedPinNotes.value.isNotEmpty()) {
            return selectedPinNotes.value.first()
        }
        return selectedOtherNotes.value.first()
    }


}
