package com.openai.voicenote.data.local

import android.util.Log
import com.openai.voicenote.data.local.dao.NoteDao
import com.openai.voicenote.data.local.repository.NoteRepository
import com.openai.voicenote.model.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteLocalDataSource @Inject constructor(private val noteRepository: NoteRepository) : NoteDataSource {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    override fun insertNote(notes: List<Note>) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                noteRepository.insertNote(notes)
            }
        }
    }

    override fun updateNote(note: Note) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                noteRepository.updateNote(note)
            }
        }
    }

    override fun getAllPinNotes(allPinNotes : (List<Note>) -> Unit) {
        coroutineScope.launch {
            val noteList = withContext(Dispatchers.IO) {
                noteRepository.getAllPinNotes()
            }
            allPinNotes(noteList)
        }
    }

    override fun getAllOtherNotes(allOtherNotes : (List<Note>) -> Unit) {
        coroutineScope.launch {
            val noteList = withContext(Dispatchers.IO) {
                noteRepository.getAllOtherNotes()
            }
            withContext(Dispatchers.Main) {
                allOtherNotes(noteList)
            }
        }
    }

    override fun togglePinStatus(noteId: Int, pin: Boolean) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                noteRepository.togglePinStatus(noteId, pin)
            }
        }
    }

    override fun toggleArchiveStatus(noteId: Int, archive: Boolean) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                noteRepository.toggleArchiveStatus(noteId, archive)
            }
        }
    }

    override fun deleteNote(noteId: Int) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                noteRepository.deleteNote(noteId)
            }
        }
    }
}