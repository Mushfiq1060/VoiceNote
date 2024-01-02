package com.openai.voicenote.core.data.repository

import com.openai.voicenote.core.database.dao.NoteResourceDao
import com.openai.voicenote.core.database.model.NoteResourceEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val noteResourceDao: NoteResourceDao
) {

    suspend fun insertNote(notes: List<NoteResourceEntity>) =
        noteResourceDao.insertNote(notes)

    fun getAllNotes() = noteResourceDao.getAllNotes()

    suspend fun updateNote(note: NoteResourceEntity) = noteResourceDao.updateNote(note)

    suspend fun togglePinStatus(notesId: List<Long>, pin: Boolean) =
        noteResourceDao.togglePinStatus(notesId, pin)

    suspend fun toggleArchiveStatus(notesId: List<Long>, archive: Boolean) =
        noteResourceDao.toggleArchiveStatus(notesId, archive)

    suspend fun deleteNotes(notesId: List<Long>) = noteResourceDao.deleteNotes(notesId)

}