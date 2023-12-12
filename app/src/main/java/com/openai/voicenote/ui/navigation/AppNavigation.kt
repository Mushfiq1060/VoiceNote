package com.openai.voicenote.ui.navigation

enum class Screen {
    HOME, VOICE_RECORD, NOTE_EDIT
}

sealed class NavigationItem(val route: String) {

    object Home : NavigationItem(Screen.HOME.name)

    object VoiceRecord : NavigationItem(Screen.VOICE_RECORD.name)

    object NoteEdit : NavigationItem(Screen.NOTE_EDIT.name)

}