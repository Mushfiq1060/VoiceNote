package com.openai.voicenote.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openai.voicenote.presentation.dataLayer.DataLayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataLayer: DataLayer
) : ViewModel() {

    fun addDataLayerListener() {
        viewModelScope.launch {
            dataLayer.addListener()
        }
    }

    fun removeDataLayerListener() {
        viewModelScope.launch {
            dataLayer.removeListener()
        }
    }

}