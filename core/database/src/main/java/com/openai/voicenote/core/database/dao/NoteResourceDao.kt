package com.openai.voicenote.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.openai.voicenote.core.database.model.NoteResourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteResourceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(notes: List<NoteResourceEntity>)

    @Query("SELECT * FROM note_table")
    fun observeAllNotes(): Flow<List<NoteResourceEntity>>

    @Query("SELECT * FROM note_table WHERE Pin= :pin")
    fun observeAllPinNotes(pin: Boolean = true): Flow<List<NoteResourceEntity>>

    @Query("SELECT * FROM note_table WHERE Pin= :pin")
    fun observeAllOtherNotes(pin: Boolean = false): Flow<List<NoteResourceEntity>>

    @Update
    suspend fun updateNote(note: NoteResourceEntity)

    @Query("UPDATE note_table SET Pin= :pin WHERE NoteId IN (:notesId)")
    suspend fun togglePinStatus(notesId: List<Long>, pin: Boolean)

    @Query("UPDATE note_table SET Archive= :archive WHERE NoteId IN (:notesId)")
    suspend fun toggleArchiveStatus(notesId: List<Long>, archive: Boolean)

    @Query("DELETE FROM note_table WHERE NoteId In (:notesId)")
    suspend fun deleteNotes(notesId: List<Long>)

}