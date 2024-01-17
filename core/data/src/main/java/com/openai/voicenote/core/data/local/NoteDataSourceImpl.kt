package com.openai.voicenote.core.data.local

import com.openai.voicenote.core.data.local.repository.NoteRepository
import com.openai.voicenote.core.database.entities.mapToNoteResourceEntity
import com.openai.voicenote.core.model.NoteResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteDataSourceImpl @Inject constructor(
    private val noteRepository: NoteRepository
) : NoteDataSource {
    override suspend fun insertNote(notes: List<NoteResource>): List<Long> {
        return noteRepository.insertNote(notes.mapToNoteResourceEntity())
    }

    override suspend fun updateNote(note: NoteResource) {
        noteRepository.updateNote(note.mapToNoteResourceEntity())
    }

    override suspend fun deleteNotes(notesId: List<Long>) {
        noteRepository.deleteNotes(notesId)
    }

    override suspend fun getNoteById(noteId: Long): NoteResource {
        return noteRepository.getNoteById(noteId)
    }

    override suspend fun togglePinStatus(notesId: List<Long>, pin: Boolean) {
        noteRepository.togglePinStatus(notesId, pin)
    }

    override suspend fun toggleArchiveStatus(notesId: List<Long>, archive: Boolean) {
        noteRepository.toggleArchiveStatus(notesId, archive)
    }

    override fun observeAllPinNotes(): Flow<List<NoteResource>> {
        return noteRepository.observeAllPinNotes()
    }

    override fun observeAllOtherNotes(): Flow<List<NoteResource>> {
        return noteRepository.observeAllOtherNotes()
    }

    override suspend fun makeCopyOfNote(noteId: Long) {
        val note = getNoteById(noteId)
        note.noteId = null
        note.editTime = System.currentTimeMillis()
        insertNote(listOf(note))
    }

}