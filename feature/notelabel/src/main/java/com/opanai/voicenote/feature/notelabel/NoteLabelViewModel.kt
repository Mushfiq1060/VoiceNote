package com.opanai.voicenote.feature.notelabel

import androidx.compose.ui.state.ToggleableState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.opanai.voicenote.feature.notelabel.navigation.NOTES_ID_LIST
import com.openai.voicenote.core.common.utils.Utils.fromJson
import com.openai.voicenote.core.model.LabelResource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class NoteLabelUiState(
    val label: LabelResource,
    val checkStatus: ToggleableState
)

@HiltViewModel
class NoteLabelViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    init {
        val argString = savedStateHandle.get<String>(NOTES_ID_LIST)
        if (argString != null) {
            val noteIdList = argString.fromJson(Array<Long>::class.java).toList()
            noteIdList.forEach {

            }
        }
    }

}