package com.openai.voicenote.data.local

import com.openai.voicenote.model.Note

interface NoteDataSource {

    fun insertSingleNote(note : Note, getInsertNoteId : (Long) -> Unit)

    fun insertMultipleNote(notes : List<Note>)

    fun updateNote(note : Note)

    fun getAllPinNotes(allPinNotes : (List<Note>) -> Unit)

    fun getAllOtherNotes(allOtherNotes : (List<Note>) -> Unit)

    fun togglePinStatus(noteId: Long, pin: Boolean)

    fun toggleArchiveStatus(noteId : Long, archive : Boolean)

    fun deleteNote(noteId : Long, resultCallback : (Int) -> Unit)

    fun deleteNotes(notesId : List<Long>, resultCallback : (Int) -> Unit)

}