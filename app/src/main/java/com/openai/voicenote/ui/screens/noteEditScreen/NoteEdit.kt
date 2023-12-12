package com.openai.voicenote.ui.screens.noteEditScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.openai.voicenote.R
import com.openai.voicenote.model.Note
import com.openai.voicenote.ui.navigation.BackPressHandler
import com.openai.voicenote.ui.navigation.NavigationItem
import com.openai.voicenote.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEdit(
    navHostController: NavHostController,
    note: Note?,
    speechToText: String?,
    noteEditViewModel: NoteEditViewModel = hiltViewModel()
) {

    val noteEditUiState by noteEditViewModel.uiState.collectAsState()

    if (speechToText != null && !noteEditViewModel.getCompose()) {
        noteEditViewModel.setCompose()
        noteEditViewModel.updateNoteText(speechToText)
    } else if (note != null && !noteEditViewModel.getCompose()) {
        noteEditViewModel.setCompose()
        noteEditViewModel.setCurrentNote(note)
    }

    BackPressHandler {
        navHostController.navigate(NavigationItem.Home.route) {
            popUpTo(Screen.HOME.name) {
                inclusive = true
            }
        }
    }

    Box {
        Image(
            modifier = Modifier
                .fillMaxSize(),
            painter = painterResource(id = R.drawable.origami),
            contentDescription = "background_image",
            contentScale = ContentScale.FillBounds
        )
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = Color.Transparent,
                    ),
                    title = {

                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navHostController.navigate(NavigationItem.Home.route) {
                                    popUpTo(Screen.HOME.name) {
                                        inclusive = true
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "back button"
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                noteEditViewModel.togglePinOfNote()
                            }
                        ) {
                            Image(
                                painter = painterResource(
                                    id = checkPinStatus(noteEditUiState.currentNotePinStatus)
                                ),
                                contentDescription = "pin note",
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        IconButton(
                            onClick = {
                                //Archive note
                            }
                        ) {
                            Image(
                                painter = painterResource(
                                    id = checkArchiveStatus(noteEditUiState.currentNoteArchiveStatus)
                                ),
                                contentDescription = "pin note",
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .background(color = Color.Transparent)
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                TextField(
                    value = noteEditViewModel.titleText,
                    placeholder = {
                        Text(
                            text = "Title",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    onValueChange = { titleText ->
                        noteEditViewModel.updateTitleText(titleText)
                    },
                    textStyle = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
                TextField(
                    value = noteEditViewModel.noteText,
                    placeholder = {
                        Text(
                            text = "Note",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onValueChange = { noteText ->
                        noteEditViewModel.updateNoteText(noteText)
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    maxLines = Int.MAX_VALUE
                )
            }
        }
    }
}

fun checkPinStatus(currentNotePin: Boolean): Int {
    if (currentNotePin) {
        return R.drawable.filled_pin_24
    }
    return R.drawable.pin_24
}

fun checkArchiveStatus(currentNoteArchive: Boolean): Int {
    if (currentNoteArchive) {
        return R.drawable.unarchive_24
    }
    return R.drawable.archive_24
}
