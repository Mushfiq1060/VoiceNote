package com.openai.voicenote.core.data.local

import com.openai.voicenote.core.database.entities.relations.NoteLabelCrossRef
import com.openai.voicenote.core.database.entities.relations.NoteWithLabels
import com.openai.voicenote.core.model.NoteResource
import kotlinx.coroutines.flow.Flow

interface NoteDataSource {

    suspend fun insertNote(notes: List<NoteResource>): List<Long>

    suspend fun insertNoteLabelCrossRef(notesId: List<Long>, labelId: Long)

    suspend fun deleteCrossRefWithNotesId(notesId: List<Long>)

    fun observeAllNotes(): Flow<List<NoteResource>>

    fun observeAllNoteWithLabels(): Flow<List<NoteResource>>

    fun observeAllPinNotes(): Flow<List<NoteResource>>

    fun observeAllPinNotesWithLabels(): Flow<List<NoteResource>>

    fun observeAllOtherNotes(): Flow<List<NoteResource>>

    fun observeAllOtherNotesWithLabels(): Flow<List<NoteResource>>

    suspend fun getNoteById(noteId: Long): NoteResource

    suspend fun updateNote(note: NoteResource)

    suspend fun togglePinStatus(notesId: List<Long>, pin: Boolean)

    suspend fun toggleArchiveStatus(notesId: List<Long>, archive: Boolean)

    suspend fun deleteNotes(notesId: List<Long>)

    suspend fun makeCopyOfNote(noteId: Long)

}