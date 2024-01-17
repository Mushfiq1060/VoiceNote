package com.openai.voicenote.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.openai.voicenote.core.database.entities.NoteResourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(notes: List<NoteResourceEntity>): List<Long>

    @Update
    suspend fun updateNote(note: NoteResourceEntity)

    @Query("DELETE FROM note_table WHERE noteId IN (:notesId)")
    suspend fun deleteNotes(notesId: List<Long>)

    @Query("SELECT * FROM note_table WHERE noteId = :noteId")
    suspend fun getNoteById(noteId: Long): NoteResourceEntity

    @Query("UPDATE note_table SET pin = :pin WHERE noteId IN (:notesId)")
    suspend fun togglePinStatus(notesId: List<Long>, pin: Boolean)

    @Query("UPDATE note_table SET archive = :archive WHERE noteId IN (:notesId)")
    suspend fun toggleArchiveStatus(notesId: List<Long>, archive: Boolean)

    @Query("SELECT * FROM note_table WHERE pin = :pin")
    fun observeAllPinNotes(pin: Boolean = true): Flow<List<NoteResourceEntity>>

    @Query("SELECT * FROM note_table WHERE pin = :pin")
    fun observeAllOtherNotes(pin: Boolean = false): Flow<List<NoteResourceEntity>>

}