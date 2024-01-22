package com.openai.voicenote.presentation.ui.screens.addNote.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.wear.compose.navigation.composable
import com.openai.voicenote.presentation.ui.screens.addNote.AddNoteRoute

const val ADD_NOTE_ROUTE = "add_note_route"

fun NavController.navigateToAddNote() = navigate(ADD_NOTE_ROUTE)

fun NavGraphBuilder.addNoteScreen(
    onPopBack: () -> Unit
) {
    composable(
        route = ADD_NOTE_ROUTE
    ) {
        AddNoteRoute(
            onPopBack = onPopBack
        )
    }
}