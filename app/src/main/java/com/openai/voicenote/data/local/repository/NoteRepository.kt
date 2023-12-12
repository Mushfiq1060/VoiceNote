package com.openai.voicenote.data.local.repository

import com.openai.voicenote.data.local.dao.NoteDao
import com.openai.voicenote.model.Note
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(private val noteDao: NoteDao) {

    suspend fun insertSingleNote(note: Note) = noteDao.insertSingleNote(note)

    suspend fun insertMultipleNote(notes: List<Note>) = noteDao.insertMultipleNote(notes)

    suspend fun updateNote(note: Note) = noteDao.updateNote(note)

    suspend fun getAllPinNotes() = noteDao.getAllPinNotes()

    suspend fun getAllOtherNotes() = noteDao.getAllOtherNotes()

    suspend fun togglePinStatus(noteId: Long, pin: Boolean) = noteDao.togglePinStatus(noteId, pin)

    suspend fun updatePinStatus(notesId: List<Long>, pin: Boolean) =
        noteDao.updatePinStatus(notesId, pin)

    suspend fun toggleArchiveStatus(noteId: Long, archive: Boolean) =
        noteDao.toggleArchiveStatus(noteId, archive)

    suspend fun deleteNote(noteId: Long) = noteDao.deleteNote(noteId)

    suspend fun deleteNotes(notesId: List<Long>) = noteDao.deleteNotes(notesId)

}