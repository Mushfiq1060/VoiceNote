package com.openai.voicenote.feature.noteedit.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.openai.voicenote.feature.noteedit.NoteEditRoute

const val NOTE_TO_STRING = "noteToString"
const val NOTE_EDIT_ROUTE = "note_edit_route"

fun NavController.navigateToNoteEdit(navOptions: NavOptions, noteString: String) =
    navigate("$NOTE_EDIT_ROUTE/$noteString", navOptions)

fun NavGraphBuilder.noteEditScreen(
    onBackClick: () -> Unit
) {
    composable(
        route = "${NOTE_EDIT_ROUTE}/{$NOTE_TO_STRING}",
        arguments = listOf(
            navArgument(NOTE_TO_STRING) { type = NavType.StringType }
        )
    ) {
        NoteEditRoute(
            onBackClick = onBackClick
        )
    }
}