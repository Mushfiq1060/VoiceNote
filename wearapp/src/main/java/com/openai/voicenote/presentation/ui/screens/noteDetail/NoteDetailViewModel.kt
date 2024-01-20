package com.openai.voicenote.presentation.ui.screens.noteDetail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.openai.voicenote.presentation.ui.screens.noteDetail.navigation.NOTE_FETCH_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        val noteFetchType = savedStateHandle.get<String>(NOTE_FETCH_TYPE)
        if (noteFetchType != null) {
            Log.i("TAGG", noteFetchType)
        }
    }

}