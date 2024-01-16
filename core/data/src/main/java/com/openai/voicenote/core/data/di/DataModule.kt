package com.openai.voicenote.core.data.di

import com.openai.voicenote.core.data.local.LabelDataSource
import com.openai.voicenote.core.data.local.LabelLocalDataSource
import com.openai.voicenote.core.data.local.NoteDataSource
import com.openai.voicenote.core.data.local.NoteLocalDataSource
import com.openai.voicenote.core.data.local.repository.UserDataRepository
import com.openai.voicenote.core.data.local.repository.UserDataRepositoryImpl
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