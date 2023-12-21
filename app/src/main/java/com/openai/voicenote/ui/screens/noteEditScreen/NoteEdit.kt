package com.openai.voicenote.ui.screens.noteEditScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.openai.voicenote.R
import com.openai.voicenote.model.Note
import com.openai.voicenote.ui.component.BottomSheet
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
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    if (speechToText != null && !noteEditViewModel.getCompose()) {
        noteEditViewModel.setCompose()
        noteEditViewModel.updateSpeechToText(speechToText)
        noteEditViewModel.addHistory("", speechToText)
    } else if (note != null && !noteEditViewModel.getCompose()) {
        noteEditViewModel.setCompose()
        noteEditViewModel.setCurrentNote(note)
        noteEditViewModel.addHistory(note.title, note.description)
    } else if (!noteEditViewModel.getCompose()) {
        noteEditViewModel.setCompose()
        noteEditViewModel.addHistory("", "")
    }

    BackPressHandler {
        navHostController.navigate(NavigationItem.Home.route) {
            popUpTo(Screen.HOME.name) {
                inclusive = true
            }
        }
    }

    Box {
        if (noteEditUiState.backgroundImageId != -1) {
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                painter = painterResource(id = noteEditUiState.backgroundImageId),
                contentDescription = "background_image",
                contentScale = ContentScale.FillBounds
            )
        }
        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .systemBarsPadding()
                .imePadding(),
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = Color.Transparent,
                    ),
                    title = {

                    },
                    scrollBehavior = scrollBehavior,
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
            },
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth(),
                    actions = {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(0.33f)
                            ) {
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(
                                        modifier = Modifier.size(28.dp),
                                        painter = painterResource(id = R.drawable.add_box_24),
                                        contentDescription = "add box",
                                        tint = Color.Black
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        noteEditViewModel.toggleBottomSheetState(true)
                                    }
                                ) {
                                    Icon(
                                        modifier = Modifier.size(28.dp),
                                        painter = painterResource(id = R.drawable.color_palette_24),
                                        contentDescription = "color palette",
                                        tint = Color.Black
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .weight(0.33f)
                                    .fillMaxHeight(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                if (!noteEditUiState.isNoteEditStarted) {
                                    Text(
                                        text = "Edited " + noteEditUiState.editedTime,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                else {
                                    IconButton(
                                        onClick = {
                                            noteEditViewModel.undoHistory()
                                        },
                                        enabled = noteEditUiState.isUndoPossible
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.undo_24),
                                            contentDescription = "undo",
                                            tint = if (noteEditUiState.isUndoPossible) Color.Black else Color.Gray
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            noteEditViewModel.redoHistory()
                                        },
                                        enabled = noteEditUiState.isRedoPossible
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.redo_24),
                                            contentDescription = "redo",
                                            tint = if (noteEditUiState.isRedoPossible) Color.Black else Color.Gray
                                        )
                                    }
                                }
                            }
                            Row(
                                modifier = Modifier.weight(0.33f),
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(
                                        modifier = Modifier.size(28.dp),
                                        painter = painterResource(id = R.drawable.more_vert_24),
                                        contentDescription = "option",
                                        tint = Color.Black
                                    )
                                }
                            }
                        }
                    },
                    containerColor = Color.Transparent
                )
            }
        ) { paddingValues ->
            if (noteEditUiState.sheetOpenState) {
                BottomSheet(
                    onDismiss = {
                        noteEditViewModel.toggleBottomSheetState(false)
                    }
                ) {
                    PaletteBottomSheet() {
                        if (it == R.drawable.no_image) {
                            noteEditViewModel.changeBackgroundImage(-1)
                        }
                        else {
                            noteEditViewModel.changeBackgroundImage(it)
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .background(color = Color.Transparent)
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .imePadding()
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

@Composable
fun PaletteBottomSheet(onBackgroundImageSelect: (id: Int) -> Unit) {
    Column {
        Text(
            text = stringResource(id = R.string.background),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp)
        )
        BottomSheetRow() {
            onBackgroundImageSelect(it)
        }
    }
}

@Composable
fun BottomSheetRow(onBackgroundImageSelect: (id: Int) -> Unit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(backgroundImageData) {
            Image(
                painter = painterResource(id = it),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .clickable(
                        onClick = {
                            onBackgroundImageSelect(it)
                        }
                    )
            )
        }
    }
}

val backgroundImageData = listOf(
    R.drawable.no_image,
    R.drawable.one,
    R.drawable.two,
    R.drawable.three,
    R.drawable.four,
    R.drawable.five,
    R.drawable.six,
    R.drawable.seven,
    R.drawable.eight,
    R.drawable.nine
).toMutableList()

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
