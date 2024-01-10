package com.openai.voicenote.core.data.local.repository

import com.openai.voicenote.core.model.NoteView
import com.openai.voicenote.core.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    val userData: Flow<UserData>

    suspend fun toggleNoteView(noteView: NoteView)

}