package com.openai.voicenote.ui.screens.homeScreen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState : StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun toggleGridView() {
        _uiState.update { currentState ->
            currentState.copy(
                isGridEnable = !currentState.isGridEnable
            )
        }
    }

//    fun toggleThemeDialog() {
//        _uiState.update {currentState ->
//            currentState.copy(
//                isThemeDialogOpen = !currentState.isThemeDialogOpen
//            )
//        }
//    }

}