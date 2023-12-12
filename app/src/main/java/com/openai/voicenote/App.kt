package com.openai.voicenote

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.openai.voicenote.model.DrawerMenu
import com.openai.voicenote.ui.component.AppNavDrawer
import com.openai.voicenote.ui.navigation.AppNavHost
import com.openai.voicenote.ui.navigation.NavigationItem
import com.openai.voicenote.ui.theme.VoiceNoteTheme

enum class DrawerMenuOption {
    HOME,
    CREATE_NEW_LABEL,
    ARCHIVE,
    TRASH,
    SETTINGS,
    HELP_AND_FEEDBACK
}

@Composable
fun App(
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    navHostController: NavHostController = rememberNavController()
) {

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    VoiceNoteTheme {
        Surface {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    AppNavDrawer(
                        items = drawerContent,
                        drawerState = drawerState,
                        selectedIndex = selectedIndex
                    ) {
                        when (it) {
                            DrawerMenuOption.HOME -> {
                                selectedIndex = 0
                                navHostController.navigate(NavigationItem.Home.route)
                            }
                            DrawerMenuOption.CREATE_NEW_LABEL -> {
                                selectedIndex = 1
                                // go to create a new label screen
                            }
                            DrawerMenuOption.ARCHIVE -> {
                                selectedIndex = 2
                                // go to archive screen
                            }
                            DrawerMenuOption.TRASH -> {
                                selectedIndex = 3
                                // go to trash screen
                            }
                            DrawerMenuOption.SETTINGS -> {
                                selectedIndex = 4
                                // go to settings screen
                            }
                            DrawerMenuOption.HELP_AND_FEEDBACK -> {
                                selectedIndex = 5
                                // go to help & feedback screen
                            }
                        }
                    }
                }
            ) {
                AppNavHost(navHostController = navHostController, drawerState = drawerState)
            }
        }
    }

}

val drawerContent = listOf<DrawerMenu>(
    DrawerMenu(
        title = R.string.notes,
        icon = R.drawable.note_24,
        menuOption = DrawerMenuOption.HOME
    ),
    DrawerMenu(
        title = R.string.create_new_label,
        icon = R.drawable.add_24,
        menuOption = DrawerMenuOption.CREATE_NEW_LABEL
    ),
    DrawerMenu(
        title = R.string.archive,
        icon = R.drawable.archive_24,
        menuOption = DrawerMenuOption.ARCHIVE
    ),
    DrawerMenu(
        title = R.string.trash,
        icon = R.drawable.delete_24,
        menuOption = DrawerMenuOption.TRASH
    ),
    DrawerMenu(
        title = R.string.settings,
        icon = R.drawable.settings_24,
        menuOption = DrawerMenuOption.SETTINGS
    ),
    DrawerMenu(
        title = R.string.help,
        icon = R.drawable.help_24,
        menuOption = DrawerMenuOption.HELP_AND_FEEDBACK
    )
)