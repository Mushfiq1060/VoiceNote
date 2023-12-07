package com.openai.voicenote.data.local

import com.openai.voicenote.model.Note

interface NoteDataSource {

    fun insertNote(notes : List<Note>)

    fun updateNote(note : Note)

    fun getAllPinNotes(allPinNotes : (List<Note>) -> Unit)

    fun getAllOtherNotes(allOtherNotes : (List<Note>) -> Unit)

    fun togglePinStatus(noteId : Int, pin : Boolean)

    fun toggleArchiveStatus(noteId : Int, archive : Boolean)

    fun deleteNote(noteId : Int)

}