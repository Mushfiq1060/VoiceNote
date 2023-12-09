package com.openai.voicenote.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.openai.voicenote.model.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSingleNote(note : Note) : Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMultipleNote(notes : List<Note>)

    @Update
    suspend fun updateNote(note : Note)

    @Query("SELECT * FROM note_table WHERE Pin= :pin AND Archive= :archive ORDER BY EditTime DESC")
    suspend fun getAllPinNotes(pin : Boolean = true, archive: Boolean = false) : List<Note>

    @Query("SELECT * FROM note_table WHERE Pin= :pin AND Archive= :archive ORDER BY EditTime DESC")
    suspend fun getAllOtherNotes(pin : Boolean = false, archive: Boolean = false) : List<Note>

    @Query("UPDATE note_table SET Pin= :pin WHERE NoteId= :noteId")
    suspend fun togglePinStatus(noteId : Long, pin : Boolean)

    @Query("UPDATE note_table SET Archive= :archive WHERE NoteId= :noteId")
    suspend fun toggleArchiveStatus(noteId : Int, archive : Boolean)

    @Query("DELETE FROM note_table WHERE NoteId= :noteId")
    suspend fun deleteNote(noteId : Int)

}