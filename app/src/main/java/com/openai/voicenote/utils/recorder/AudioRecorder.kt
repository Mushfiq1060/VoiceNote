package com.openai.voicenote.utils.recorder

import java.io.File

interface AudioRecorder {

    fun startRecording(outputFile : File)

    fun stopRecording()

}