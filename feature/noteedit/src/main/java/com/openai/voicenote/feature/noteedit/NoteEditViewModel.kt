package com.openai.voicenote.feature.noteedit

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import com.openai.voicenote.core.common.utils.QueryDeBouncer
import com.openai.voicenote.core.common.utils.Utils
import com.openai.voicenote.core.designsystem.icon.VnColor
import com.openai.voicenote.core.designsystem.icon.VnImage
import com.openai.voicenote.core.model.NoteResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
class NoteEditViewModel @Inject constructor(): ViewModel() {

    private val mUiState = MutableStateFlow(NoteEditUiState())
    val uiState: StateFlow<NoteEditUiState> = mUiState.asStateFlow()

    private val noteAutoSaveOrUpdateHandler = QueryDeBouncer<NoteResource>(
        durationInMilliseconds = 300,
        onValue = {
            noteAutoSaveOrUpdate(it)
        }
    )

    private fun noteAutoSaveOrUpdate(note: NoteResource) {

    }

    fun togglePinOfNote() {

    }

    fun toggleArchiveOfNote() {

    }

    fun changeTitleText(text: String) {
        mUiState.update {
            it.copy(
                titleText = text
            )
        }
    }

    fun changeNoteText(text: String) {
        mUiState.update {
            it.copy(
                noteText = text
            )
        }
    }

    fun onTextChange(type: InputType, text: String) {
        if (type == InputType.TITLE) {
            changeTitleText(text)
        } else if (type == InputType.NOTE) {
            changeNoteText(text)
        }
    }

    fun onBackgroundColorChange(id: Int) {
        mUiState.update {
            it.copy(
                backgroundColor = id,
                backgroundImageId = VnImage.bgImageList[0].id
            )
        }
    }

    fun onBackgroundImageChange(id: Int) {
        mUiState.update {
            it.copy(
                backgroundImageId = id,
                backgroundColor = VnColor.bgColorList[0]
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
            BottomAppBarItem.UNDO -> {

            }
            BottomAppBarItem.REDO -> {

            }
            BottomAppBarItem.MORE_VERT -> {

            }
        }
    }

}