package com.openai.voicenote.presentation.dataLayer

import android.content.Context
import android.util.Log
import com.openai.voicenote.presentation.model.NoteResource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataLayer @Inject constructor(
    @ApplicationContext context: Context
) {

    fun getNotes(): Flow<List<NoteResource>> {
        return flow {
            emit(listOf(
                NoteResource(
                    noteId = 1,
                    title = "Cenotaph is nowhere",
                    description = "I silently laugh at my own cenotaph, " +
                            "and out of the caverns of rain, like a child " +
                            "from the womb, like a ghost from the tomb, " +
                            "I arise and unbuild it again...",
                    editTime = System.currentTimeMillis(),
                    pin = false,
                    archive = false,
                    backgroundColor = -1,
                    backgroundImage = -1,
                    labelList = listOf()
                ),
                NoteResource(
                    noteId = 2,
                    title = "Two",
                    description = "This is note two",
                    editTime = System.currentTimeMillis(),
                    pin = false,
                    archive = false,
                    backgroundColor = -1,
                    backgroundImage = -1,
                    labelList = listOf()
                ),
                NoteResource(
                    noteId = 1,
                    title = "Three",
                    description = "This is note three",
                    editTime = System.currentTimeMillis(),
                    pin = false,
                    archive = false,
                    backgroundColor = -1,
                    backgroundImage = -1,
                    labelList = listOf()
                ),
            ))
        }
    }

}