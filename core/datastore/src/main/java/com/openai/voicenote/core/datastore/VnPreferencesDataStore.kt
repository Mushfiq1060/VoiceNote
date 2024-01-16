package com.openai.voicenote.core.datastore

import androidx.datastore.core.DataStore
import com.openai.voicenote.core.datastoreproto.NoteViewProto
import com.openai.voicenote.core.datastoreproto.UserPreferences
import com.openai.voicenote.core.datastoreproto.copy
import com.openai.voicenote.core.model.NoteView
import com.openai.voicenote.core.model.UserData
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VnPreferencesDataStore @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {

    val userData = userPreferences.data
        .map {
            UserData(
                noteView = when (it.noteView) {
                    NoteViewProto.NOTE_LIST_VIEW -> NoteView.LIST
                    NoteViewProto.NOTE_GRID_VIEW -> NoteView.GRID
                    else -> NoteView.GRID
                }
            )
        }

    /**
     * [setNoteView] function toggle view state in here.
     * So pass current state of [noteView] from UI layer
     */
    suspend fun setNoteView(noteView: NoteView) {
        userPreferences.updateData {
            it.copy {
                this.noteView = when (noteView) {
                    NoteView.LIST -> NoteViewProto.NOTE_GRID_VIEW
                    NoteView.GRID -> NoteViewProto.NOTE_LIST_VIEW
                }
            }
        }
    }

}