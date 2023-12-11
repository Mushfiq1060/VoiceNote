package com.openai.voicenote.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.openai.voicenote.model.Note
import com.openai.voicenote.ui.screens.homeScreen.Home
import com.openai.voicenote.ui.screens.noteEditScreen.NoteEdit
import com.openai.voicenote.ui.screens.voiceRecordScreen.VoiceRecord
import com.openai.voicenote.utils.Utils.fromJson

@Composable
fun AppNavHost(
    navHostController: NavHostController,
    startDestination: String = NavigationItem.Home.route
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {
        composable(NavigationItem.Home.route) {
            Home(navHostController = navHostController)
        }
        composable(NavigationItem.VoiceRecord.route) {
            VoiceRecord(navHostController = navHostController)
        }
        composable(
            NavigationItem.NoteEdit.route + "/{noteString}/{speechToText}/{fromWhichPage}",
            arguments = listOf(
                navArgument("noteString") {type = NavType.StringType},
                navArgument("speechToText") {type = NavType.StringType},
                navArgument("fromWhichPage") {type = NavType.IntType}
            )
        ) { navBackStack ->
            when (navBackStack.arguments?.getInt("fromWhichPage")) {
                1 -> { // by click on note in the home screen
                    val noteString = navBackStack.arguments?.getString("noteString")
                    val note = noteString?.fromJson(Note::class.java)
                    NoteEdit(navHostController = navHostController, note = note, speechToText = null)
                }
                2 -> { // from voice record screen
                    val speechToText = navBackStack.arguments?.getString("speechToText")
                    NoteEdit(navHostController = navHostController, note = null, speechToText = speechToText)
                }
                3 -> { // by click on fab in the home screen
                    NoteEdit(navHostController = navHostController, note = null, speechToText = null)
                }
            }
        }
    }
}