package com.opanai.voicenote.feature.notelabel

import androidx.compose.ui.state.ToggleableState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opanai.voicenote.feature.notelabel.navigation.NOTES_ID_LIST
import com.openai.voicenote.core.common.utils.Utils.fromJson
import com.openai.voicenote.core.data.local.LabelDataSource
import com.openai.voicenote.core.data.local.NoteLabelDataSource
import com.openai.voicenote.core.model.LabelResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UiState(
    val label: LabelResource,
    val checkStatus: ToggleableState
)

sealed interface NoteLabelUiState {

    data object Loading : NoteLabelUiState

    data class Success(
        val list: List<UiState>
    ) : NoteLabelUiState

}

@HiltViewModel
class NoteLabelViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    labelDataSource: LabelDataSource,
    private val noteLabelDataSource: NoteLabelDataSource
) : ViewModel() {

    private var noteIdList = mutableListOf<Long>()
    private var labelPerNoteId = mutableListOf<List<Long>>()
    private var intersectingLabelId = mutableSetOf<Long>()
    private var nonIntersectingLabelId = mutableSetOf<Long>()

    init {
        val argString = savedStateHandle.get<String>(NOTES_ID_LIST)
        if (argString != null) {
            noteIdList = argString.fromJson(Array<Long>::class.java).toMutableList()
            if (noteIdList.isNotEmpty()) {
                getLabelsIdByNoteId()
            }
        }
    }

    private val labelFlow = MutableStateFlow(false)

    val labelUiState: StateFlow<NoteLabelUiState> = combine(
        labelFlow,
        labelDataSource.observeAllLabels()
    ) { _, labels ->
        val list = getUiStateList(labels)
        NoteLabelUiState.Success(list)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = NoteLabelUiState.Loading
        )

    private fun getUiStateList(labels: List<LabelResource>): List<UiState> {
        val list = mutableListOf<UiState>()
        labels.forEach {
            if (intersectingLabelId.contains(it.labelId)) {
                list.add(UiState(label = it, checkStatus = ToggleableState.On))
            }
            else if (nonIntersectingLabelId.contains(it.labelId)) {
                list.add(UiState(label = it, checkStatus = ToggleableState.Indeterminate))
            }
            else {
                list.add(UiState(label = it, checkStatus = ToggleableState.Off))
            }
        }
        return list.toList()
    }

    private fun getLabelsIdByNoteId() {
        viewModelScope.launch {
            noteIdList.forEach {
                labelPerNoteId.add(noteLabelDataSource.getAllLabelIdByNoteId(it))
            }
            intersectingLabelId = getIntersectingLabelId()
            nonIntersectingLabelId = getNonIntersectingLabelId()
            labelFlow.update { !it }
        }
    }

    private fun getNonIntersectingLabelId(): MutableSet<Long> {
        val list = mutableSetOf<Long>()
        labelPerNoteId.forEach {
            it.forEach { id ->
                if (!intersectingLabelId.contains(id)) {
                    list.add(id)
                }
            }
        }
        return list.toMutableSet()
    }

    private fun getIntersectingLabelId(): MutableSet<Long> {
        var list = labelPerNoteId[0].toSet()
        for (i in 1..<labelPerNoteId.size) {
            list = labelPerNoteId[i].intersect(list)
        }
        return list.toMutableSet()
    }

    fun onCheckCLick(labelId: Long, check: Boolean) {
        if (check) {
            addLabelIdToNote(labelId)
        } else {
            removeLabelIdFromNote(labelId)
        }
    }

    private fun addLabelIdToNote(labelId: Long) {
        viewModelScope.launch {
            noteLabelDataSource.insertNoteLabel(noteIdList, labelId)
            nonIntersectingLabelId.remove(labelId)
            intersectingLabelId.add(labelId)
            labelFlow.update { !it }
        }
    }

    private fun removeLabelIdFromNote(labelId: Long) {
        viewModelScope.launch {
            noteLabelDataSource.removeNoteLabelRow(noteIdList, listOf(labelId))
            nonIntersectingLabelId.remove(labelId)
            intersectingLabelId.remove(labelId)
            labelFlow.update { !it }
        }
    }

}