package com.openai.voicenote.presentation.ui.screens.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.wear.compose.navigation.composable
import com.openai.voicenote.presentation.ui.screens.home.HomeRoute

const val HOME_ROUTE = "home_route"

fun NavController.navigateToHome() = navigate(HOME_ROUTE)

fun NavGraphBuilder.homeScreen(
    onNavigateToAddNote: () -> Unit,
    onNavigateToViewNote: (String) -> Unit,
    onNavigateToHelp: () -> Unit,
) {
    composable(
        route = HOME_ROUTE
    ) {
        HomeRoute(
            onNavigateToAddNote = onNavigateToAddNote,
            onNavigateToViewNote = onNavigateToViewNote,
            onNavigateToHelp = onNavigateToHelp
        )
    }
}