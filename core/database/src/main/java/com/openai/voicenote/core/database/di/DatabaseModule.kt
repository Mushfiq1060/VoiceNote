package com.openai.voicenote.core.database.di

import android.content.Context
import androidx.room.Room
import com.openai.voicenote.core.database.VnDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun provideVnDatabase(@ApplicationContext context: Context): VnDatabase {
        return Room.databaseBuilder(
            context = context,
            VnDatabase::class.java,
            "voice_note_database"
        ).build()
    }

}