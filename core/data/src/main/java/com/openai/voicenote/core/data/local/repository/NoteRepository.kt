package com.openai.voicenote.core.data.local.repository

import com.openai.voicenote.core.database.dao.NoteDao
import com.openai.voicenote.core.database.dao.NoteLabelDao
import com.openai.voicenote.core.database.entities.NoteResourceEntity
import com.openai.voicenote.core.database.entities.mapToNoteResource
import com.openai.voicenote.core.database.entities.relations.mapToNoteResource
import com.openai.voicenote.core.model.NoteResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val noteLabelDao: NoteLabelDao
) {
    suspend fun insertNote(notes: List<NoteResourceEntity>) =
        noteDao.insertNote(notes)

    suspend fun updateNote(note: NoteResourceEntity) {
        noteDao.updateNote(note)
    }

    suspend fun deleteNotes(notesId: List<Long>) {
        noteDao.deleteNotes(notesId)
        noteLabelDao.deleteNoteLabelByNoteId(notesId)
    }

    suspend fun getNoteById(noteId: Long): NoteResource {
        return noteDao.getNoteById(noteId).mapToNoteResource()
    }

    suspend fun togglePinStatus(notesId: List<Long>, pin: Boolean) {
        noteDao.togglePinStatus(notesId, pin)
    }

    suspend fun toggleArchiveStatus(notesId: List<Long>, archive: Boolean) {
        noteDao.toggleArchiveStatus(notesId, archive)
    }

    fun observeAllPinNotes(): Flow<List<NoteResource>> {
        return noteDao.observeAllPinNotes().flatMapLatest {
            noteLabelDao.observeAllPinNoteWithLabels().map {
                it.mapToNoteResource()
            }
        }
    }

    fun observeAllOtherNotes(): Flow<List<NoteResource>> {
        return noteDao.observeAllOtherNotes().flatMapLatest {
            noteLabelDao.observeAllOtherNoteWithLabels().map {
                it.mapToNoteResource()
            }
        }
    }

}