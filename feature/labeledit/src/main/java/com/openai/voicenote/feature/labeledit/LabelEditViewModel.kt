package com.openai.voicenote.feature.labeledit

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
import javax.inject.Inject

data class LabelEditUiState(
    val labelList: List<LabelResource>,
    val enableEditing: Boolean
)


@HiltViewModel
class LabelEditViewModel @Inject constructor(
    private val labelDataSource: LabelDataSource
) : ViewModel() {

    private val editEnable = MutableStateFlow(false)
    private val labelListState: Flow<List<LabelResource>> = labelDataSource.observeAllLabels()

    val labelEditUiState = combine(
        labelListState,
        editEnable
    ) { list, enableEdit ->
        LabelEditUiState(labelList = list, enableEditing = enableEdit)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = LabelEditUiState(labelList = listOf(), enableEditing = true)
    )
    fun addLabel() {

    }

}