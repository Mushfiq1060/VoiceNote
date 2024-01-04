package com.openai.voicenote.feature.noteedit

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openai.voicenote.core.designsystem.icon.VnIcons
import com.openai.voicenote.core.designsystem.icon.VnImage
import com.openai.voicenote.core.designsystem.theme.VnTheme
import com.openai.voicenote.core.ui.component.BackgroundPickerBottomSheet

@Composable
fun NoteEditRoute(
    modifier: Modifier = Modifier,
    viewModel: NoteEditViewModel = hiltViewModel()
) {
    val noteEditUiState by viewModel.uiState.collectAsStateWithLifecycle()

    NoteEditeScreen(
        noteEditUiState = noteEditUiState,
        onClickPinIcon = { viewModel.togglePinOfNote() },
        onClickArchiveIcon = { viewModel.toggleArchiveOfNote() },
        onClickBottomAppBarItem = { viewModel.onClickBottomAppBarItem(it) },
        dismissBottomSheet = { viewModel.dismissBottomSheet() },
        onBackgroundColorChange = { viewModel.onBackgroundColorChange(it) },
        onBackgroundImageChange = { viewModel.onBackgroundImageChange(it) }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NoteEditeScreen(
    modifier: Modifier = Modifier,
    noteEditUiState: NoteEditUiState,
    onClickPinIcon: () -> Unit,
    onClickArchiveIcon: () -> Unit,
    onClickBottomAppBarItem: (item: BottomAppBarItem) -> Unit,
    dismissBottomSheet: () -> Unit,
    onBackgroundColorChange: (Int) -> Unit,
    onBackgroundImageChange: (Int) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(noteEditUiState.backgroundColor))
    ) {
        if (noteEditUiState.backgroundImageId != VnImage.bgImageList[0].id) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(
                    id = VnImage.bgImageList[noteEditUiState.backgroundImageId].drawableId
                ),
                contentDescription = "background image",
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
                NoteEditTopAppBar(
                    scrollBehavior = scrollBehavior,
                    notePinStatus = noteEditUiState.notePinStatus,
                    noteArchiveStatus = noteEditUiState.noteArchiveStatus,
                    onClickBackIcon = {
                        // navigate to previous screen not voice note screen
                    },
                    onClickPinIcon = { onClickPinIcon() },
                    onClickArchiveIcon = { onClickArchiveIcon() }
                )
            },
            bottomBar = {
                NoteEditBottomAppBar(
                    isNoteEditStarted = noteEditUiState.isNoteEditStarted,
                    editTime = noteEditUiState.editTime,
                    isUndoPossible = noteEditUiState.isUndoPossible,
                    isRedoPossible = noteEditUiState.isRedoPossible,
                    onClickBottomAppBarItem = { onClickBottomAppBarItem(it) }
                )
            }
        ) { paddingValues ->
            when (noteEditUiState.bottomSheetType) {
                BottomSheetType.BACKGROUND_PICKER_OPTION -> {
                    BackgroundPickerBottomSheet(
                        onBottomSheetDismiss = { dismissBottomSheet() },
                        onBackgroundColorChange = { onBackgroundColorChange(it) },
                        onBackgroundImageChange = { onBackgroundImageChange(it) },
                        selectedBackgroundColor = noteEditUiState.backgroundColor,
                        selectedBackgroundImage = noteEditUiState.backgroundImageId
                    )
                }
                BottomSheetType.MORE_VERT_OPTION -> {
                    // open bottom sheet while click on more icon
                }
                BottomSheetType.ADD_BOX_OPTION -> {
                    // open bottom sheet while click on add box icon
                }
                else -> { /* do nothing */ }
            }
            Spacer(modifier = Modifier.padding(paddingValues))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    notePinStatus: Boolean,
    noteArchiveStatus: Boolean,
    onClickBackIcon: () -> Unit,
    onClickPinIcon: () -> Unit,
    onClickArchiveIcon: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.Transparent
        ),
        title = {  },
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = { onClickBackIcon() }) {
                Icon(
                    painter = painterResource(id = VnIcons.arrowBack),
                    contentDescription = "back button",
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        actions = {
            IconButton(onClick = { onClickPinIcon() }) {
                Icon(
                    painter = painterResource(
                        id = if (notePinStatus) VnIcons.filledPin else VnIcons.pin
                    ),
                    contentDescription = "pin status",
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(onClick = { onClickArchiveIcon() }) {
                Icon(
                    painter = painterResource(
                        id = if (noteArchiveStatus) VnIcons.unarchive else VnIcons.archive
                    ),
                    contentDescription = "archive status",
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    )
}

@Composable
fun NoteEditBottomAppBar(
    isNoteEditStarted: Boolean,
    editTime: String,
    isUndoPossible: Boolean,
    isRedoPossible: Boolean,
    onClickBottomAppBarItem: (item: BottomAppBarItem) -> Unit
) {
    BottomAppBar(
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth(),
        containerColor = Color.Transparent,
        actions = {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(0.33f)
                ) {
                    IconButton(
                        onClick = {
                            onClickBottomAppBarItem(BottomAppBarItem.ADD_BOX)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = VnIcons.addBox),
                            contentDescription = "add box",
                            modifier = Modifier.size(28.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    IconButton(
                        onClick = {
                            onClickBottomAppBarItem(BottomAppBarItem.COLOR_PALETTE)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = VnIcons.colorPalette),
                            contentDescription = "color palette",
                            modifier = Modifier.size(28.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
                UndoRedoSection(
                    modifier = Modifier
                        .weight(0.33f)
                        .fillMaxHeight(),
                    editTime = editTime,
                    isNoteEditStarted = isNoteEditStarted,
                    isUndoPossible = isUndoPossible,
                    isRedoPossible = isRedoPossible,
                    onClickBottomAppBarItem = { onClickBottomAppBarItem(it) }
                )
                Row(
                    modifier = Modifier.weight(0.33f),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = {
                            onClickBottomAppBarItem(BottomAppBarItem.MORE_VERT)
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            painter = painterResource(id = VnIcons.moreVert),
                            contentDescription = "option",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun UndoRedoSection(
    modifier: Modifier = Modifier,
    editTime: String,
    isNoteEditStarted: Boolean,
    isUndoPossible: Boolean,
    isRedoPossible: Boolean,
    onClickBottomAppBarItem: (item: BottomAppBarItem) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (!isNoteEditStarted) {
            Text(
                text = "Edited $editTime",
                style = MaterialTheme.typography.bodySmall,
            )
        } else {
            IconButton(
                onClick = { onClickBottomAppBarItem(BottomAppBarItem.UNDO) },
                enabled = isUndoPossible
            ) {
                Icon(
                    painter = painterResource(id = VnIcons.undo),
                    contentDescription = "undo",
                    modifier = Modifier.size(24.dp),
                    tint = if (isUndoPossible) {
                        MaterialTheme.colorScheme.onBackground
                    } else {
                        MaterialTheme.colorScheme.outline
                    }
                )
            }
            IconButton(
                onClick = { onClickBottomAppBarItem(BottomAppBarItem.REDO) },
                enabled = isRedoPossible
            ) {
                Icon(
                    painter = painterResource(id = VnIcons.redo),
                    contentDescription = "redo",
                    modifier = Modifier.size(24.dp),
                    tint = if (isRedoPossible) {
                        MaterialTheme.colorScheme.onBackground
                    } else {
                        MaterialTheme.colorScheme.outline
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun NoteEditTopAppBarPreview() {
    VnTheme {
        Surface {
            NoteEditTopAppBar(
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                notePinStatus = true,
                noteArchiveStatus = false,
                onClickBackIcon = {  },
                onClickPinIcon = {  },
                onClickArchiveIcon = {  }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteEditBottomAppBarPreview() {
    VnTheme {
        Surface {
            NoteEditBottomAppBar(
                isNoteEditStarted = true,
                editTime = "Dec 30, 2023",
                isUndoPossible = true,
                isRedoPossible = true,
                onClickBottomAppBarItem = {}
            )
        }
    }
}