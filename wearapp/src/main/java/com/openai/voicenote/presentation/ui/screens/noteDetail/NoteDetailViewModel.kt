package com.openai.voicenote.presentation.ui.screens.noteDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.openai.voicenote.presentation.model.NoteResource
import com.openai.voicenote.presentation.ui.screens.noteDetail.navigation.NOTE_STRING
import com.openai.voicenote.presentation.utils.Utils.fromJson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    lateinit var note: NoteResource

    init {
        val noteString = savedStateHandle.get<String>(NOTE_STRING)
        if (noteString != null) {
            note = noteString.fromJson(NoteResource::class.java)
        }
    }

}