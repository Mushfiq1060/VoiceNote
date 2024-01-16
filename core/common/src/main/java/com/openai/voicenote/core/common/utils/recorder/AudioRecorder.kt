package com.openai.voicenote.core.common.utils.recorder

import java.io.File

interface AudioRecorder {

    fun startRecording(outputFile: File, recordingStartCallback: () -> Unit)

    fun stopRecording()

}