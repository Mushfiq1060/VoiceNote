package com.openai.voicenote.core.data

import com.openai.voicenote.core.data.repository.NoteRepository
import com.openai.voicenote.core.database.model.NoteResourceEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteLocalDataSource @Inject constructor(
    private val noteRepository: NoteRepository
) : NoteDataSource {
    override suspend fun insertNote(notes: List<NoteResourceEntity>) {
        noteRepository.insertNote(notes)
    }

    override fun getAllNotes(): Flow<List<NoteResourceEntity>> {
        return noteRepository.getAllNotes()
    }

    override suspend fun updateNote(note: NoteResourceEntity) {
        noteRepository.updateNote(note)
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