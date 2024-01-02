package com.openai.voicenote.core.data

import com.openai.voicenote.core.database.model.NoteResourceEntity
import kotlinx.coroutines.flow.Flow

interface NoteDataSource {

    suspend fun insertNote(notes: List<NoteResourceEntity>)

    fun getAllNotes(): Flow<List<NoteResourceEntity>>

    suspend fun updateNote(note: NoteResourceEntity)

    suspend fun togglePinStatus(notesId: List<Long>, pin: Boolean)

    suspend fun toggleArchiveStatus(notesId: List<Long>, archive: Boolean)

    suspend fun deleteNotes(notesId: List<Long>)

}