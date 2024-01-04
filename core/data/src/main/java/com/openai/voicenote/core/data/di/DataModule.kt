package com.openai.voicenote.core.data.di

import com.openai.voicenote.core.data.LabelDataSource
import com.openai.voicenote.core.data.LabelLocalDataSource
import com.openai.voicenote.core.data.NoteDataSource
import com.openai.voicenote.core.data.NoteLocalDataSource
import com.openai.voicenote.core.data.repository.UserDataRepository
import com.openai.voicenote.core.data.repository.UserDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsNoteLocalDataSource(
        noteLocalDataSource: NoteLocalDataSource
    ): NoteDataSource

    @Binds
    internal abstract fun bindsLabelLocalDataSource(
        labelLocalDataSource: LabelLocalDataSource
    ): LabelDataSource

    @Binds
    internal abstract fun bindsUserDataRepository(
        userDataRepositoryImpl: UserDataRepositoryImpl
    ): UserDataRepository

}