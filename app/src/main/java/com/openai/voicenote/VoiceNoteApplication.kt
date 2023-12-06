package com.openai.voicenote

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class VoiceNoteApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

}