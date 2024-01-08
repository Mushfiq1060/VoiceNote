package com.openai.voicenote.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.openai.voicenote.feature.noteedit.navigation.noteEditScreen
import com.openai.voicenote.feature.notes.navigation.NOTES_ROUTE
import com.openai.voicenote.feature.notes.navigation.notesScreen
import com.openai.voicenote.ui.VnAppState

@Composable
fun VnNavHost(
    appState: VnAppState,
    startDestination: String = NOTES_ROUTE
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        notesScreen(
            goToNoteEditScreen = {
                appState.navigateToNoteEdit(0, it)
            },
            goToVoiceNoteScreen = {  },
            onDrawerOpen = { appState.openDrawer() }
        )
        noteEditScreen(
            onBackClick = { navController.popBackStack() }
        )
    }

}