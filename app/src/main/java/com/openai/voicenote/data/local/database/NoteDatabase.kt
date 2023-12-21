package com.openai.voicenote.data.local.database

import androidx.room.RoomDatabase
import com.openai.voicenote.data.local.dao.NoteDao
import com.openai.voicenote.model.Note

@androidx.room.Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

}