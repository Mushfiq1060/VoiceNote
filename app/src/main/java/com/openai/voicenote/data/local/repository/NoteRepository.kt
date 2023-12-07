package com.openai.voicenote.data.local.repository

import com.openai.voicenote.data.local.dao.NoteDao
import com.openai.voicenote.model.Note
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(private val noteDao : NoteDao) {

    suspend fun insertNote(notes : List<Note>) = noteDao.insertNote(notes)

    suspend fun updateNote(note : Note) = noteDao.updateNote(note)

    suspend fun getAllPinNotes() = noteDao.getAllPinNotes()

    suspend fun getAllOtherNotes() = noteDao.getAllOtherNotes()

    suspend fun togglePinStatus(noteId : Int, pin : Boolean) = noteDao.togglePinStatus(noteId, pin)

    suspend fun toggleArchiveStatus(noteId: Int, archive : Boolean) = noteDao.toggleArchiveStatus(noteId, archive)

    suspend fun deleteNote(noteId: Int) = noteDao.deleteNote(noteId)

}