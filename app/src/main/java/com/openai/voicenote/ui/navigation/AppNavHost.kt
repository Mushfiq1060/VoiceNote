package com.openai.voicenote.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.openai.voicenote.ui.screens.homeScreen.Home
import com.openai.voicenote.ui.screens.voiceRecordScreen.VoiceRecord

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
    }
}