package com.openai.voicenote.core.data.local.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class FileRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun createFile(fileName: String): File {
        return File(context.cacheDir, fileName)
    }

}