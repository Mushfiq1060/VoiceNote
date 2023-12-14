package com.openai.voicenote.data.local

import com.openai.voicenote.data.local.repository.NoteRepository
import com.openai.voicenote.model.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteLocalDataSource @Inject constructor(private val noteRepository: NoteRepository) :
    NoteDataSource {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    override suspend fun insertSingleNote(note: Note): Long {
        return noteRepository.insertSingleNote(note)
    }

    override suspend fun insertMultipleNote(notes: List<Note>) {
        noteRepository.insertMultipleNote(notes)
    }

    override suspend fun updateNote(note: Note) {
        noteRepository.updateNote(note)
    }

    override suspend fun getAllPinNotes(): List<Note> {
        return noteRepository.getAllPinNotes()
    }

    override suspend fun getAllOtherNotes(): List<Note> {
        return noteRepository.getAllOtherNotes()
    }

    override suspend fun togglePinStatus(noteId: Long, pin: Boolean) {
        noteRepository.togglePinStatus(noteId, pin)
    }

    override suspend fun updatePinStatus(notesId: List<Long>, pin: Boolean): Int {
        return noteRepository.updatePinStatus(notesId, pin)
    }

    override suspend fun toggleArchiveStatus(noteId: Long, archive: Boolean) {
        noteRepository.toggleArchiveStatus(noteId, archive)
    }

    override suspend fun deleteNote(noteId: Long): Int {
        return noteRepository.deleteNote(noteId)
    }

    override suspend fun deleteNotes(notesId: List<Long>): Int {
        return noteRepository.deleteNotes(notesId)
    }
}