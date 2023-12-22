package com.openai.voicenote.ui.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.openai.voicenote.model.Note
import com.openai.voicenote.ui.navigation.AppDrawer
import com.openai.voicenote.ui.navigation.NavigationItem
import com.openai.voicenote.ui.screens.editLabel.EditLabel
import com.openai.voicenote.ui.screens.noteEditScreen.NoteEdit
import com.openai.voicenote.utils.Utils.fromJson

@Composable
fun RootNavGraph(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = AppDrawer.APP_DRAWER
    ) {

        composable(
            route = AppDrawer.APP_DRAWER
        ) {
            AppDrawer(navController = navController)
        }

        composable(
            WithoutNavigationDrawerItem.NoteEdit.route + "/{noteString}/{speechToText}/{fromWhichPage}",
            arguments = listOf(
                navArgument("noteString") { type = NavType.StringType },
                navArgument("speechToText") { type = NavType.StringType },
                navArgument("fromWhichPage") { type = NavType.IntType }
            )
        ) { navBackStack ->
            when (navBackStack.arguments?.getInt("fromWhichPage")) {
                1 -> { // by click on note in the home screen
                    val noteString = navBackStack.arguments?.getString("noteString")
                    val note = noteString?.fromJson(Note::class.java)
                    NoteEdit(
                        navHostController = navController,
                        note = note,
                        speechToText = null
                    )
                }

                2 -> { // from voice record screen
                    val speechToText = navBackStack.arguments?.getString("speechToText")
                    NoteEdit(
                        navHostController = navController,
                        note = null,
                        speechToText = speechToText
                    )
                }

                3 -> { // by click on fab in the home screen
                    NoteEdit(
                        navHostController = navController,
                        note = null,
                        speechToText = null
                    )
                }
            }
        }

        composable(
            route = WithoutNavigationDrawerItem.EditLabels.route
        ) {
            EditLabel(navController = navController)
        }

    }

}

object AppDrawer {
    const val APP_DRAWER = "APP_DRAWER"
}

enum class WithoutNavigationDrawerScreen {
    NOTE_EDIT,
    EDIT_LABELS,
    SETTINGS,
    HELP_AND_FEEDBACK
}

sealed class WithoutNavigationDrawerItem(val route: String) {

    object NoteEdit: WithoutNavigationDrawerItem(WithoutNavigationDrawerScreen.NOTE_EDIT.name)

    object EditLabels: WithoutNavigationDrawerItem(WithoutNavigationDrawerScreen.EDIT_LABELS.name)

    object Settings: WithoutNavigationDrawerItem(WithoutNavigationDrawerScreen.SETTINGS.name)

    object HelpAndFeedback: WithoutNavigationDrawerItem(WithoutNavigationDrawerScreen.HELP_AND_FEEDBACK.name)

}