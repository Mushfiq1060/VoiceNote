package com.openai.voicenote.di

import android.content.Context
import androidx.room.Room
import com.openai.voicenote.data.local.NoteDataSource
import com.openai.voicenote.data.local.NoteLocalDataSource
import com.openai.voicenote.data.local.dao.NoteDao
import com.openai.voicenote.data.local.database.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun providesNoteDao(noteDatabase: NoteDatabase) : NoteDao {
        return noteDatabase.noteDao()
    }

    @Provides
    fun provideNoteDatabase(@ApplicationContext context : Context) : NoteDatabase {
        return Room.databaseBuilder(
            context = context,
            NoteDatabase::class.java,
            "note_app_database"
        ).build()
    }

    @Provides
    fun providesNoteDataSource(noteLocalDataSource: NoteLocalDataSource) : NoteDataSource {
        return noteLocalDataSource
    }

}