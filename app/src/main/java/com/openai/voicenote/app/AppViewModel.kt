package com.openai.voicenote.app

import androidx.lifecycle.ViewModel
import com.openai.voicenote.model.Label
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(): ViewModel() {

    private val mUiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = mUiState.asStateFlow()
    private var count = 0

    fun getAllLabels() {
        val list = List(count) {
            Label((Math.random() * Long.MAX_VALUE).toLong(), it.toString())
        }
        mUiState.update { currentState ->
            currentState.copy(
                labelList = list
            )
        }
        count++
    }

    fun changeSelectedIndex(index: Int) {
        mUiState.update { currentState ->
            currentState.copy(
                selectedIndex = index
            )
        }
    }

    fun changeLabelSelectedIndex(index: Int) {
        mUiState.update { currentState ->
            currentState.copy(
                selectedLabelIndex = index
            )
        }
    }

    fun changeGestureState(state: Boolean) {
        mUiState.update { currentState ->
            currentState.copy(
                gestureState = state
            )
        }
    }

}