package com.openai.voicenote.feature.notes.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.openai.voicenote.feature.notes.NotesRoute

const val NOTES_ROUTE = "notes_route"

fun NavController.navigateToNotes(navOptions: NavOptions) = navigate(NOTES_ROUTE, navOptions)

fun NavGraphBuilder.notesScreen(
    goToNoteEditScreen: (note: String) -> Unit,
    goToVoiceNoteScreen: () -> Unit,
    goToNoteLabelScreen: () -> Unit,
    onDrawerOpen: () -> Unit
) {
    composable(route = NOTES_ROUTE) {
        NotesRoute(
            goToNoteEditScreen = goToNoteEditScreen,
            goToVoiceNoteScreen = goToVoiceNoteScreen,
            goToNoteLabelScreen = goToNoteLabelScreen,
            onDrawerOpen = onDrawerOpen
        )
    }
}