package com.openai.voicenote.ui.navigation.graphs

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.openai.voicenote.ui.screens.homeScreen.Home

@Composable
fun DrawerNavGraph(
    navController: NavHostController,
    drawerState: DrawerState,
    startDestination: String = NavigationDrawerItem.Home.route
) {

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(
            route = NavigationDrawerItem.Home.route
        ) {
            Home(navController = navController, drawerState = drawerState)
        }

        composable(
            route = NavigationDrawerItem.LabelsDescription.route
        ) {
            // go to labels description screen labels info
        }

        composable(
            route = NavigationDrawerItem.Archive.route
        ) {
            // go to archive screen
        }

        composable(
            route = NavigationDrawerItem.Trash.route
        ) {
            // go to trash screen
        }

    }

}

enum class NavigationDrawerScreen {
    HOME,
    LABELS_DESCRIPTION,
    ARCHIVE,
    TRASH
}

sealed class NavigationDrawerItem(val route: String) {

    object Home: NavigationDrawerItem(NavigationDrawerScreen.HOME.name)

    object LabelsDescription: NavigationDrawerItem(NavigationDrawerScreen.LABELS_DESCRIPTION.name)

    object Archive: NavigationDrawerItem(NavigationDrawerScreen.ARCHIVE.name)

    object Trash: NavigationDrawerItem(NavigationDrawerScreen.TRASH.name)

}