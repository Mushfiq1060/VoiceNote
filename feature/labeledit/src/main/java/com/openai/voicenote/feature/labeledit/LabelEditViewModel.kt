package com.openai.voicenote.feature.labeledit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openai.voicenote.core.data.LabelDataSource
import com.openai.voicenote.core.model.LabelResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LabelEditUiState(
    val labelList: List<LabelResource>,
    val enableEditing: Boolean,
    val createLabelText: String,
    val updateLabelText: String,
    val updateLabelIndex: Int,
)

@HiltViewModel
class LabelEditViewModel @Inject constructor(
    private val labelDataSource: LabelDataSource
) : ViewModel() {

    private val editEnable = MutableStateFlow(true)
    private val createLabelTextState = MutableStateFlow("")
    private val updateLabelTextState = MutableStateFlow("")
    private val updateLabelIndex = MutableStateFlow(-1)
    val shouldShowDeleteDialog = MutableStateFlow(false)

    val labelEditUiState = combine(
        labelDataSource.observeAllLabels(),
        editEnable,
        createLabelTextState,
        updateLabelTextState,
        updateLabelIndex
    ) { list, enableEdit, labelText, updateText, updateIndex ->
        LabelEditUiState(
            labelList = list,
            enableEditing = enableEdit,
            createLabelText = labelText,
            updateLabelText = updateText,
            updateLabelIndex = updateIndex
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = LabelEditUiState(
            labelList = listOf(),
            enableEditing = true,
            createLabelText = "",
            updateLabelText = "",
            updateLabelIndex = -1
        )
    )

    fun toggleDeleteDialog() {
        shouldShowDeleteDialog.update { !it }
    }

    fun toggleEnableEditing() {
        editEnable.update { !it }
        updateLabelIndex.update { -1 }
    }

    fun onChangeTextInCreateLabel(text: String) {
        createLabelTextState.update { text }
    }

    fun onChangeUpdateLabelText(text: String) {
        updateLabelTextState.update { text }
    }

    fun onChangeUpdateLabelIndex(index: Int) {
        updateLabelIndex.update { index }
        updateLabelTextState.update { labelEditUiState.value.labelList[index].labelName }
        editEnable.update { false }
    }

    fun addLabel() {
        val newLabel = LabelResource(
            labelId = null,
            labelName = createLabelTextState.value
        )
        createLabelTextState.update { "" }
        editEnable.update { false }
        viewModelScope.launch {
            labelDataSource.insertLabel(listOf(newLabel))
        }
    }

    private fun resetState() {
        editEnable.update { false }
        createLabelTextState.update { "" }
        updateLabelTextState.update { "" }
        updateLabelIndex.update { -1 }
    }

    fun updateLabel(index: Int) {
        val curLabel = labelEditUiState.value.labelList[index].copy()
        curLabel.labelName = updateLabelTextState.value
        resetState()
        viewModelScope.launch {
            labelDataSource.updateLabel(curLabel)
        }
    }

    fun deleteLabel() {
        val label = labelEditUiState.value.labelList[updateLabelIndex.value]
        toggleDeleteDialog()
        resetState()
        viewModelScope.launch {
            labelDataSource.deleteLabels(listOf(label.labelId!!))
        }
    }

}