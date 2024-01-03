package com.openai.voicenote.core.data

import com.openai.voicenote.core.data.repository.NoteRepository
import com.openai.voicenote.core.database.model.mapToNoteResource
import com.openai.voicenote.core.database.model.mapToNoteResourceEntity
import com.openai.voicenote.core.model.NoteResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteLocalDataSource @Inject constructor(
    private val noteRepository: NoteRepository
) : NoteDataSource {
    override suspend fun insertNote(notes: List<NoteResource>) {
        noteRepository.insertNote(notes.mapToNoteResourceEntity())
    }

    override fun observeAllNotes(): Flow<List<NoteResource>> {
        return noteRepository.observeAllNotes().map {
            it.mapToNoteResource()
        }
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
}