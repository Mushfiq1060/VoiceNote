package com.openai.voicenote.app

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.openai.voicenote.R
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
    appViewModel: AppViewModel = hiltViewModel(),
    navHostController: NavHostController = rememberNavController()
) {

    val appUiState by appViewModel.uiState.collectAsState()

    val drawerState: DrawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed,
        confirmStateChange =  {
            appViewModel.getAllLabels()
            true
        }
    )

    VoiceNoteTheme {
        Surface {
            ModalNavigationDrawer(
                drawerState = drawerState,
                gesturesEnabled = appUiState.gestureState,
                drawerContent = {
                    AppNavDrawer(
                        items = drawerContent,
                        labelItems = appUiState.labelList,
                        drawerState = drawerState,
                        selectedIndex = appUiState.selectedIndex,
                        selectedLabelIndex = appUiState.selectedLabelIndex
                    ) { menuOrdinal, labelOrdinal ->
                        if (labelOrdinal == -1) {
                            appViewModel.changeLabelSelectedIndex(-1)
                            when (menuOrdinal) {
                                DrawerMenuOption.HOME.ordinal -> {
                                    appViewModel.changeSelectedIndex(menuOrdinal)
                                    navHostController.navigate(NavigationItem.Home.route)
                                }

                                DrawerMenuOption.CREATE_NEW_LABEL.ordinal -> {
                                    appViewModel.changeSelectedIndex(menuOrdinal)
                                    // go to create a new label screen
                                }

                                DrawerMenuOption.ARCHIVE.ordinal -> {
                                    appViewModel.changeSelectedIndex(menuOrdinal)
                                    // go to archive screen
                                }

                                DrawerMenuOption.TRASH.ordinal -> {
                                    appViewModel.changeSelectedIndex(menuOrdinal)
                                    // go to trash screen
                                }

                                DrawerMenuOption.SETTINGS.ordinal -> {
                                    appViewModel.changeSelectedIndex(menuOrdinal)
                                    // go to settings screen
                                }

                                DrawerMenuOption.HELP_AND_FEEDBACK.ordinal -> {
                                    appViewModel.changeSelectedIndex(menuOrdinal)
                                    // go to help & feedback screen
                                }
                            }
                        }
                        else {
                            appViewModel.changeLabelSelectedIndex(labelOrdinal)
                            appViewModel.changeSelectedIndex(-1)
                        }
                    }
                }
            ) {
                AppNavHost(
                    navHostController = navHostController,
                    drawerState = drawerState
                ) {
                    appViewModel.changeGestureState(it)
                }
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