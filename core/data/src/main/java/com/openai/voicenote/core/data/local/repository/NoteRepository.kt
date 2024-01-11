package com.openai.voicenote.core.data.local.repository

import com.openai.voicenote.core.database.dao.NoteResourceDao
import com.openai.voicenote.core.database.entities.NoteResourceEntity
import com.openai.voicenote.core.database.entities.relations.NoteLabelCrossRef
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val noteResourceDao: NoteResourceDao,
) {

    suspend fun insertNote(notes: List<NoteResourceEntity>) =
        noteResourceDao.insertNote(notes)

    suspend fun insertNoteLabelCrossRef(crossRefs: List<NoteLabelCrossRef>) =
        noteResourceDao.insertNoteLabelCrossRef(crossRefs)

    suspend fun deleteCrossRefWithNotesId(notesId: List<Long>, labelId: Long) =
        noteResourceDao.deleteCrossRefWithNotesId(notesId, labelId)

    fun observeAllNotes() = noteResourceDao.observeAllNotes()

    fun observeAllNoteWithLabels() = noteResourceDao.observeAllNoteWithLabels()

    fun observeAllPinNotes() = noteResourceDao.observeAllPinNotes()

    fun observeAllPinNotesWithLabels() = noteResourceDao.observeAllPinNotesWithLabels()

    fun observeAllOtherNotes() = noteResourceDao.observeAllOtherNotes()

    fun observeAllOtherNotesWithLabels() = noteResourceDao.observeAllOtherNotesWithLabels()

    suspend fun getNoteById(noteId: Long) = noteResourceDao.getNoteById(noteId)

    suspend fun updateNote(note: NoteResourceEntity) = noteResourceDao.updateNote(note)

    suspend fun togglePinStatus(notesId: List<Long>, pin: Boolean) =
        noteResourceDao.togglePinStatus(notesId, pin)

    suspend fun toggleArchiveStatus(notesId: List<Long>, archive: Boolean) =
        noteResourceDao.toggleArchiveStatus(notesId, archive)

    suspend fun deleteNotes(notesId: List<Long>) = noteResourceDao.deleteNotes(notesId)

    suspend fun getNotesIdByLabelId(labelId: Long): List<Long> =
        noteResourceDao.getNotesByLabelId(labelId)

}