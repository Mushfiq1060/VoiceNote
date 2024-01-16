package com.openai.voicenote.core.common.utils.player

import java.io.File

interface AudioPlayer {

    fun startPlayer(file: File, completionCallback: () -> Unit)

    fun pausePlayer()

    fun stopPlayer()

}