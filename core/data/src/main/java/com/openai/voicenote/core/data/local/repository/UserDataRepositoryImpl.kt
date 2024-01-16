package com.openai.voicenote.core.data.local.repository

import com.openai.voicenote.core.datastore.VnPreferencesDataStore
import com.openai.voicenote.core.model.NoteView
import com.openai.voicenote.core.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(
    private val vnPreferencesDataStore: VnPreferencesDataStore
) : UserDataRepository {

    override val userData: Flow<UserData>
        get() = vnPreferencesDataStore.userData

    override suspend fun toggleNoteView(noteView: NoteView) {
        vnPreferencesDataStore.setNoteView(noteView)
    }

}