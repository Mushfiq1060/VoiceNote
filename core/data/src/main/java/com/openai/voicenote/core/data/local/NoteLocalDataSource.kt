package com.openai.voicenote.core.data.local

import com.openai.voicenote.core.data.local.repository.NoteRepository
import com.openai.voicenote.core.database.entities.mapToNoteResource
import com.openai.voicenote.core.database.entities.mapToNoteResourceEntity
import com.openai.voicenote.core.database.entities.relations.NoteLabelCrossRef
import com.openai.voicenote.core.database.entities.relations.mapToNoteResource
import com.openai.voicenote.core.model.NoteResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteLocalDataSource @Inject constructor(
    private val noteRepository: NoteRepository
) : NoteDataSource {
    override suspend fun insertNote(notes: List<NoteResource>): List<Long> {
        return noteRepository.insertNote(notes.mapToNoteResourceEntity())
    }

    override suspend fun insertNoteLabelCrossRef(notesId: List<Long>, labelId: Long) {
        val crossRef = mutableListOf<NoteLabelCrossRef>()
        notesId.forEach {
            crossRef.add(NoteLabelCrossRef(noteId = it, labelId = labelId))
        }
        noteRepository.insertNoteLabelCrossRef(crossRef)
    }

    override suspend fun deleteCrossRefWithNotesId(notesId: List<Long>) {
        noteRepository.deleteCrossRefWithNotesId(notesId)
    }

    override fun observeAllNotes(): Flow<List<NoteResource>> {
        return noteRepository.observeAllNotes().map {
            it.mapToNoteResource()
        }
    }

    override fun observeAllNoteWithLabels(): Flow<List<NoteResource>> {
        return noteRepository.observeAllNoteWithLabels().map {
            it.mapToNoteResource()
        }
    }

    override fun observeAllPinNotes(): Flow<List<NoteResource>> {
        return noteRepository.observeAllPinNotes().map {
            it.mapToNoteResource()
        }
    }

    override fun observeAllPinNotesWithLabels(): Flow<List<NoteResource>> {
        return noteRepository.observeAllPinNotesWithLabels().map {
            it.mapToNoteResource()
        }
    }

    override fun observeAllOtherNotes(): Flow<List<NoteResource>> {
        return noteRepository.observeAllOtherNotes().map {
            it.mapToNoteResource()
        }
    }

    override fun observeAllOtherNotesWithLabels(): Flow<List<NoteResource>> {
        return noteRepository.observeAllOtherNotesWithLabels().map {
            it.mapToNoteResource()
        }
    }

    override suspend fun getNoteById(noteId: Long): NoteResource {
        return noteRepository.getNoteById(noteId).mapToNoteResource()
    }

    override suspend fun updateNote(note: NoteResource) {
        noteRepository.updateNote(note.mapToNoteResourceEntity())
    }

    override suspend fun togglePinStatus(notesId: List<Long>, pin: Boolean) {
        noteRepository.togglePinStatus(notesId, pin)
    }

    override suspend fun toggleArchiveStatus(notesId: List<Long>, archive: Boolean) {
        noteRepository.toggleArchiveStatus(notesId, archive)
    }

    override suspend fun deleteNotes(notesId: List<Long>) {
        noteRepository.deleteNotes(notesId)
    }

    override suspend fun makeCopyOfNote(noteId: Long) {
        val note = getNoteById(noteId)
        note.noteId = null
        note.editTime = System.currentTimeMillis()
        insertNote(listOf(note))
    }
}