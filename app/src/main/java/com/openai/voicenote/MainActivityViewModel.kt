package com.openai.voicenote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openai.voicenote.core.data.local.LabelDataSource
import com.openai.voicenote.core.data.local.repository.UserDataRepository
import com.openai.voicenote.core.model.LabelResource
import com.openai.voicenote.core.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    labelDataSource: LabelDataSource,
    userDataRepository: UserDataRepository,
) : ViewModel() {

    val uiState: StateFlow<MainActivityUiState> = combine(
        userDataRepository.userData,
        labelDataSource.observeAllLabels()
    ) { userData, items ->
        MainActivityUiState.Success(userData = userData, labelItems = items)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = MainActivityUiState.Loading
    )

}

sealed interface MainActivityUiState {

    data object Loading: MainActivityUiState

    data class Success(
        val userData: UserData,
        val labelItems: List<LabelResource>
    ): MainActivityUiState

}