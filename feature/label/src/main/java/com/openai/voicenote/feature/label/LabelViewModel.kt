package com.openai.voicenote.feature.label

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openai.voicenote.core.data.LabelDataSource
import com.openai.voicenote.core.data.repository.UserDataRepository
import com.openai.voicenote.core.model.NoteView
import com.openai.voicenote.feature.label.navigation.LABEL_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class LabelViewModel @Inject constructor(
    private val labelDataSource: LabelDataSource,
    private val userDataRepository: UserDataRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var argLabelId by Delegates.notNull<Long>()
    init {
        argLabelId = savedStateHandle.get<Long>(LABEL_ID)!!
    }

    val contextMenuState = MutableStateFlow(false)
    val noteViewState = userDataRepository.userData.map { it.noteView }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = NoteView.GRID
        )
    val labelName = labelDataSource.getLabelNameById(argLabelId)

    fun toggleNoteView() {
        viewModelScope.launch {
            userDataRepository.toggleNoteView(noteViewState.value)
        }
    }

    fun toggleContextMenuState() {
        contextMenuState.update { !it }
    }

    fun deleteLabel() {
        viewModelScope.launch {
            labelDataSource.deleteLabels(listOf(argLabelId))
        }
    }

}