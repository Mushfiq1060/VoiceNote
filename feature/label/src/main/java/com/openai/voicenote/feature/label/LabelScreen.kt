package com.openai.voicenote.feature.label
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openai.voicenote.core.designsystem.icon.VnIcons
import com.openai.voicenote.core.designsystem.theme.VnTheme
import com.openai.voicenote.core.model.NoteView
import com.openai.voicenote.core.ui.component.ConfirmationDialog
import com.openai.voicenote.core.ui.component.EmptyNoteList
import com.openai.voicenote.core.ui.component.NoteFeedUiState
import com.openai.voicenote.core.ui.component.NoteList

enum class LabelAppBarItem {
    DRAWER,
    NOTE_VIEW,
    CONTEXT_MENU
}

@Composable
fun LabelRoute(
    onBackClick: () -> Unit,
    onDrawerOpen: () -> Unit,
    viewModel: LabelViewModel = hiltViewModel()
) {
    val feedState by viewModel.feedState.collectAsStateWithLifecycle()
    val labelName by viewModel.labelName.collectAsStateWithLifecycle("")
    val noteViewState by viewModel.noteViewState.collectAsStateWithLifecycle()
    val contextMenuState by viewModel.contextMenuState.collectAsStateWithLifecycle()
    val shouldShowDeleteDialog by viewModel.shouldShowDeleteDialog.collectAsStateWithLifecycle()
    val shouldShowRenameDialog by viewModel.shouldShowRenameDialog.collectAsStateWithLifecycle()
    val renameLabelText by viewModel.renameLabelText.collectAsStateWithLifecycle()

    LabelScreen(
        feedState = feedState,
        labelName = labelName,
        noteViewState = noteViewState,
        contextMenuState = contextMenuState,
        shouldShowDeleteDialog = shouldShowDeleteDialog,
        shouldShowRenameDialog = shouldShowRenameDialog,
        renameLabelText = renameLabelText,
        onRenameLabelTextChange = { viewModel.updateRenameLabelText(it) },
        onClickRenameLabel = {
            viewModel.toggleContextMenuState()
            viewModel.toggleRenameDialogState(labelName)
        },
        onClickDeleteLabel = {
            viewModel.toggleContextMenuState()
            viewModel.toggleDeleteDialogState()
        },
        onClickLabelAppBarItem = {
            when (it) {
                LabelAppBarItem.DRAWER -> {
                    onDrawerOpen()
                }
                LabelAppBarItem.CONTEXT_MENU -> {
                    viewModel.toggleContextMenuState()
                }
                LabelAppBarItem.NOTE_VIEW -> {
                    viewModel.toggleNoteView()
                }
            }
        },
        onDeleteDialogConfirmClick = {
            viewModel.toggleDeleteDialogState()
            viewModel.deleteLabel()
            onBackClick()
        },
        onDeleteDialogCancelClick = { viewModel.toggleDeleteDialogState() },
        onRenameDialogConfirmClick = { viewModel.updateLabel() },
        onRenameDialogCancelClick = { viewModel.toggleRenameDialogState(labelName) }
    )
}

@Composable
fun LabelScreen(
    feedState: NoteFeedUiState,
    labelName: String,
    noteViewState: NoteView,
    contextMenuState: Boolean,
    shouldShowDeleteDialog: Boolean,
    shouldShowRenameDialog: Boolean,
    renameLabelText: String,
    onRenameLabelTextChange: (text: String) -> Unit,
    onClickRenameLabel: () -> Unit,
    onClickDeleteLabel: () -> Unit,
    onClickLabelAppBarItem: (item: LabelAppBarItem) -> Unit,
    onDeleteDialogConfirmClick: () -> Unit,
    onDeleteDialogCancelClick: () -> Unit,
    onRenameDialogConfirmClick: () -> Unit,
    onRenameDialogCancelClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            LabelScreenTopAppBar(
                labelName = labelName,
                isContextMenuOpen = contextMenuState,
                noteViewState = noteViewState,
                onClick = { onClickLabelAppBarItem(it) },
                onClickRenameLabel = { onClickRenameLabel() },
                onClickDeleteLabel = { onClickDeleteLabel() }
            )
        }
    ) { paddingValues ->
        if (shouldShowDeleteDialog) {
            ConfirmationDialog(
                heading = "Delete label?",
                description = "We'll delete this label and remove it from all of your keep notes. Your notes won't be deleted.",
                confirmButtonText = "Delete",
                dismissButtonText = "Cancel",
                onConfirmClick = { onDeleteDialogConfirmClick() },
                onDismissClick = { onDeleteDialogCancelClick() }
            )
        }
        if (shouldShowRenameDialog) {
            RenameLabelDialog(
                renameLabelText = renameLabelText,
                onTextChange = { onRenameLabelTextChange(it) },
                onConfirmClick = { onRenameDialogConfirmClick() },
                onDismissClick = { onRenameDialogCancelClick() }
            )
        }
        when (feedState) {
            is NoteFeedUiState.Loading -> {
                // show loader
            }
            is NoteFeedUiState.Success -> {
                if (feedState.pinnedNoteList.isEmpty() &&
                    feedState.otherNoteList.isEmpty()) {
                    EmptyNoteList(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        text = "No notes with this label yet",
                        icon = VnIcons.label
                    )
                }
                else {
                    NoteList(
                        modifier = Modifier.padding(paddingValues),
                        pinnedList = feedState.pinnedNoteList,
                        otherList = feedState.otherNoteList,
                        selectedPinnedList = mutableSetOf(),
                        selectedOthersList = mutableSetOf(),
                        noteViewState = noteViewState,
                        onClick = { _, _ -> },
                        onLongClick = { _, _ -> }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelScreenTopAppBar(
    labelName: String,
    isContextMenuOpen: Boolean,
    noteViewState: NoteView,
    onClick: (item: LabelAppBarItem) -> Unit,
    onClickRenameLabel: () -> Unit,
    onClickDeleteLabel: () -> Unit
) {
    TopAppBar(
        modifier = Modifier
            .statusBarsPadding()
            .height(48.dp),
        title = {
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = labelName,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = { onClick(LabelAppBarItem.DRAWER) }
            ) {
                Icon(
                    painter = painterResource(id = VnIcons.menu),
                    contentDescription = "menu icon",
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        actions = {
            IconButton(
                onClick = { onClick(LabelAppBarItem.NOTE_VIEW) }
            ) {
                Icon(
                    painter = painterResource(
                        id = if (noteViewState == NoteView.GRID) VnIcons.gridView else VnIcons.listView
                    ),
                    contentDescription = "view",
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(
                onClick = { onClick(LabelAppBarItem.CONTEXT_MENU) }
            ) {
                Icon(
                    painter = painterResource(id = VnIcons.moreVert),
                    contentDescription = "context menu",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(28.dp)
                )
                DropdownMenu(
                    expanded = isContextMenuOpen,
                    onDismissRequest = { onClick(LabelAppBarItem.CONTEXT_MENU) }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Rename Label",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = { onClickRenameLabel() }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Delete Label",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = { onClickDeleteLabel() }
                    )
                }
            }
        }
    )
}

@Composable
fun RenameLabelDialog(
    renameLabelText: String,
    onTextChange: (text: String) -> Unit,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismissClick() },
        confirmButton = {
            Button(
                onClick = { onConfirmClick() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Rename",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissClick() }) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        title = {
            Text(
                text = "Rename Label",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        text = {
            OutlinedTextField(
                value = renameLabelText,
                onValueChange = { onTextChange(it) },
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun RenameLabelDialogPreview() {
    VnTheme {
        Surface {
            RenameLabelDialog(
                renameLabelText = "Who Cares??",
                onTextChange = {},
                onConfirmClick = {},
                onDismissClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LabelScreenTopAppBarPreview() {
    VnTheme {
        Surface {
            LabelScreenTopAppBar(
                labelName = "One",
                isContextMenuOpen = false,
                noteViewState = NoteView.GRID,
                onClick = {},
                onClickRenameLabel = {},
                onClickDeleteLabel = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LabelScreenPreview() {
    VnTheme {
        Surface {
            LabelScreen(
                feedState = NoteFeedUiState.Success(
                    selectedPinNotes = mutableSetOf(),
                    selectedOtherNotes = mutableSetOf(),
                    pinnedNoteList = listOf(),
                    otherNoteList = listOf()
                ),
                labelName = "Who cares!!",
                noteViewState = NoteView.LIST,
                contextMenuState = false,
                shouldShowDeleteDialog = false,
                shouldShowRenameDialog = false,
                renameLabelText = "",
                onRenameLabelTextChange = {},
                onClickRenameLabel = {},
                onClickDeleteLabel = {},
                onClickLabelAppBarItem = {},
                onDeleteDialogConfirmClick = {},
                onDeleteDialogCancelClick = {},
                onRenameDialogConfirmClick = {},
                onRenameDialogCancelClick = {}
            )
        }
    }
}