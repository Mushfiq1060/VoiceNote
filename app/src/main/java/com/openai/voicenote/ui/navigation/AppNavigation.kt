package com.openai.voicenote.ui.navigation

enum class Screen {
    HOME,
    VOICE_RECORD
}

sealed class NavigationItem(val route: String) {

    object Home : NavigationItem(Screen.HOME.name)

    object VoiceRecord: NavigationItem(Screen.VOICE_RECORD.name)

}