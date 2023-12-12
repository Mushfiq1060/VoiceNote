package com.openai.voicenote.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.openai.voicenote.data.local.dao.NoteDao
import com.openai.voicenote.data.local.database.converters.ColorConverters
import com.openai.voicenote.model.Note

@androidx.room.Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ColorConverters::class)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

}