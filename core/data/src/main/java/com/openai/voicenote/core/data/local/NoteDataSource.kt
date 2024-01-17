package com.openai.voicenote.core.data.local

import com.openai.voicenote.core.model.NoteResource
import kotlinx.coroutines.flow.Flow

interface NoteDataSource {
    suspend fun insertNote(notes: List<NoteResource>): List<Long>

    suspend fun updateNote(note: NoteResource)

    suspend fun deleteNotes(notesId: List<Long>)

    suspend fun getNoteById(noteId: Long): NoteResource

    suspend fun togglePinStatus(notesId: List<Long>, pin: Boolean)

    suspend fun toggleArchiveStatus(notesId: List<Long>, archive: Boolean)

    fun observeAllPinNotes(): Flow<List<NoteResource>>

    fun observeAllOtherNotes(): Flow<List<NoteResource>>

    suspend fun makeCopyOfNote(noteId: Long)

}