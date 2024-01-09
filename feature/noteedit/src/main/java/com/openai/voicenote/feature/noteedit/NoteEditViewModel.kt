package com.openai.voicenote.feature.noteedit

import android.util.Log
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openai.voicenote.core.common.utils.QueryDeBouncer
import com.openai.voicenote.core.common.utils.Utils
import com.openai.voicenote.core.common.utils.Utils.fromJson
import com.openai.voicenote.core.data.NoteDataSource
import com.openai.voicenote.core.designsystem.icon.VnColor
import com.openai.voicenote.core.designsystem.icon.VnImage
import com.openai.voicenote.core.model.NoteResource
import com.openai.voicenote.feature.noteedit.navigation.NOTE_TO_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NoteEditUiState(
    val titleText: String = "",
    val noteText: String = "",
    val notePinStatus: Boolean = false,
    val noteArchiveStatus: Boolean = false,
    val bottomSheetType: BottomSheetType = BottomSheetType.NONE,
    val backgroundImageId: Int = VnImage.bgImageList[0].id,
    val backgroundColor: Int = Color.Transparent.toArgb(),
    val editTime: String = Utils.getFormattedTime(System.currentTimeMillis()),
    val isNoteEditStarted: Boolean = false,
    val isUndoPossible: Boolean = false,
    val isRedoPossible: Boolean = false
)

enum class BottomSheetType {
    BACKGROUND_PICKER_OPTION,
    MORE_VERT_OPTION,
    ADD_BOX_OPTION,
    NONE
}

enum class BottomAppBarItem {
    ADD_BOX,
    COLOR_PALETTE,
    MORE_VERT,
    UNDO,
    REDO,
}

enum class InputType {
    TITLE,
    NOTE
}

@HiltViewModel
class NoteEditViewModel @Inject constructor(
    private val noteDataSource: NoteDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mUiState = MutableStateFlow(NoteEditUiState())
    val uiState: StateFlow<NoteEditUiState> = mUiState.asStateFlow()

    private val history = mutableListOf<Pair<String, String>>()
    private val redoHistory = mutableListOf<Pair<String, String>>()
    private var isAddedToHistory = true
    private lateinit var currentNote: NoteResource
    private lateinit var noteAutoSaveOrUpdateHandler: QueryDeBouncer<NoteResource>

    init {
        noteAutoSaveOrUpdateHandler = QueryDeBouncer<NoteResource>(
            durationInMilliseconds = 300,
            onValue = {
                noteAutoSaveOrUpdate(it)
            }
        )
        val noteString = savedStateHandle.get<String>(NOTE_TO_STRING)
        if (noteString != null) {
            val argNote = noteString.fromJson(NoteResource::class.java)
            if (argNote.noteId != null) {
                currentNote = argNote
                mUiState.update {
                    it.copy(
                        titleText = argNote.title,
                        noteText = argNote.description,
                        notePinStatus = argNote.pin,
                        noteArchiveStatus = argNote.archive,
                        editTime = Utils.getFormattedTime(argNote.editTime),
                        backgroundColor = argNote.backgroundColor,
                        backgroundImageId = argNote.backgroundImage
                    )
                }
            }
            else {
                if (argNote.description.isNotEmpty()) {
                    /** Navigate from voice note screen */
                    updateNoteText(argNote.description, isAddedToHistory = false)
                }
                else {
                    /** Navigate from notes screen by clicking on note */
                    currentNote = argNote
                }
            }
        }
    }

    private fun noteAutoSaveOrUpdate(note: NoteResource) {
        if (::currentNote.isInitialized) {
            if (currentNote.noteId == null) {
                saveNote()
            } else {
                updateNote()
            }
        }
    }

    private fun prepareNote(): NoteResource {
        if (::currentNote.isInitialized) {
            currentNote.apply {
                title = mUiState.value.titleText
                description = mUiState.value.noteText
                editTime = System.currentTimeMillis()
                pin = mUiState.value.notePinStatus
                archive = mUiState.value.noteArchiveStatus
                backgroundColor = mUiState.value.backgroundColor
                backgroundImage = mUiState.value.backgroundImageId
            }
            return currentNote
        }
        currentNote = getDefaultNote()
        return currentNote
    }

    private fun getDefaultNote() = NoteResource(
        noteId = null,
        title = mUiState.value.titleText,
        description = mUiState.value.noteText,
        editTime = System.currentTimeMillis(),
        pin = false,
        archive = false,
        backgroundColor = VnColor.bgColorList[0].id,
        backgroundImage = VnImage.bgImageList[0].id
    )

    fun togglePinOfNote() {
        if (::currentNote.isInitialized) {
            if (currentNote.noteId != null) {
                currentNote.pin = !currentNote.pin
                viewModelScope.launch {
                    noteDataSource.togglePinStatus(listOf(currentNote.noteId!!), currentNote.pin)
                    mUiState.update {
                        it.copy(
                            notePinStatus = currentNote.pin
                        )
                    }
                }
            }
        }
    }

    fun toggleArchiveOfNote() {
        if (::currentNote.isInitialized) {
            if (currentNote.noteId != null) {
                currentNote.archive = !currentNote.archive
                viewModelScope.launch {
                    noteDataSource.toggleArchiveStatus(listOf(currentNote.noteId!!), currentNote.archive)
                    mUiState.update {
                        it.copy(
                            noteArchiveStatus = currentNote.archive
                        )
                    }
                }
            }
        }
    }

    private fun updateTitleText(text: String, isAddedToHistory: Boolean = true) {
        mUiState.update {
            it.copy(
                titleText = text,
                isNoteEditStarted = true
            )
        }
        this.isAddedToHistory = isAddedToHistory
        noteAutoSaveOrUpdateHandler.typeTValue = prepareNote()
    }

    private fun updateNoteText(text: String, isAddedToHistory: Boolean = true) {
        mUiState.update {
            it.copy(
                noteText = text,
                isNoteEditStarted = true
            )
        }
        this.isAddedToHistory = isAddedToHistory
        noteAutoSaveOrUpdateHandler.typeTValue = prepareNote()
    }

    fun onTextChange(type: InputType, text: String) {
        if (type == InputType.TITLE) {
            updateTitleText(text)
        } else if (type == InputType.NOTE) {
            updateNoteText(text)
        }
    }

    fun onBackgroundColorChange(id: Int) {
        if (!::currentNote.isInitialized) {
            currentNote = getDefaultNote()
        }
        currentNote.backgroundColor = id
        currentNote.backgroundImage = VnImage.bgImageList[0].id
        currentNote.editTime = System.currentTimeMillis()
        noteAutoSaveOrUpdateHandler.typeTValue = currentNote
        toggleBackgroundState()
    }

    fun onBackgroundImageChange(id: Int) {
        if (!::currentNote.isInitialized) {
            currentNote = getDefaultNote()
        }
        currentNote.backgroundImage = id
        currentNote.backgroundColor = VnColor.bgColorList[0].id
        currentNote.editTime = System.currentTimeMillis()
        noteAutoSaveOrUpdateHandler.typeTValue = currentNote
        toggleBackgroundState()
    }

    private fun toggleBackgroundState() {
        mUiState.update {
            it.copy(
                backgroundColor = currentNote.backgroundColor,
                backgroundImageId = currentNote.backgroundImage
            )
        }
    }

    fun dismissBottomSheet() {
        mUiState.update {
            it.copy(
                bottomSheetType = BottomSheetType.NONE
            )
        }
    }

    private fun openBackgroundPickerBottomSheet() {
        mUiState.update {
            it.copy(
                bottomSheetType = BottomSheetType.BACKGROUND_PICKER_OPTION
            )
        }
    }

    fun onClickBottomAppBarItem(item: BottomAppBarItem) {
        when (item) {
            BottomAppBarItem.ADD_BOX -> {

            }
            BottomAppBarItem.COLOR_PALETTE -> {
                openBackgroundPickerBottomSheet()
            }
            BottomAppBarItem.UNDO -> { undoHistory() }
            BottomAppBarItem.REDO -> { redoHistory() }
            BottomAppBarItem.MORE_VERT -> {

            }
        }
    }

    private fun toggleUndoRedoState() {
        mUiState.update {
            it.copy(
                isUndoPossible = history.size > 1,
                isRedoPossible = redoHistory.isNotEmpty()
            )
        }
    }

    private fun addHistory(titleText: String, noteText: String) {
        history.add(Pair(titleText, noteText))
        redoHistory.clear()
        toggleUndoRedoState()
    }

    private fun undoHistory() {
        if (history.size > 1) {
            val lastHistory = history.removeLast()
            redoHistory.add(lastHistory)
            toggleUndoRedoState()
            updateTitleText(history.last().first, false)
            updateNoteText(history.last().second, false)
        }
    }

    private fun redoHistory() {
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
            currentNote.noteId = noteDataSource.insertNote(listOf(currentNote))[0]
            if (mUiState.value.isNoteEditStarted && isAddedToHistory) {
                addHistory(currentNote.title, currentNote.description)
            }
            mUiState.update {
                it.copy(
                    editTime = Utils.getFormattedTime(currentNote.editTime)
                )
            }
        }
    }

    private fun updateNote() {
        viewModelScope.launch {
            noteDataSource.updateNote(currentNote)
            if (mUiState.value.isNoteEditStarted && isAddedToHistory) {
                addHistory(currentNote.title, currentNote.description)
            }
            mUiState.update {
                it.copy(
                    editTime = Utils.getFormattedTime(currentNote.editTime)
                )
            }
        }
    }

}