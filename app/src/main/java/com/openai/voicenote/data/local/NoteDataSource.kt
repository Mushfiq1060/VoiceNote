package com.openai.voicenote.data.local

import com.openai.voicenote.model.Note

interface NoteDataSource {

    suspend fun insertSingleNote(note: Note): Long

    suspend fun insertMultipleNote(notes: List<Note>)

    suspend fun updateNote(note: Note)

    suspend fun getAllPinNotes(): List<Note>

    suspend fun getAllOtherNotes(): List<Note>

    suspend fun togglePinStatus(noteId: Long, pin: Boolean)

    suspend fun updatePinStatus(notesId: List<Long>, pin: Boolean): Int

    suspend fun toggleArchiveStatus(noteId: Long, archive: Boolean)

    suspend fun deleteNote(noteId: Long): Int

    suspend fun deleteNotes(notesId: List<Long>): Int

}