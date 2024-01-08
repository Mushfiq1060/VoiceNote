package com.openai.voicenote.feature.label
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openai.voicenote.core.designsystem.icon.VnIcons
import com.openai.voicenote.core.designsystem.theme.VnTheme
import com.openai.voicenote.core.model.NoteView
import com.openai.voicenote.core.ui.component.EmptyNoteList

enum class LabelAppBarItem {
    DRAWER,
    NOTE_VIEW,
    CONTEXT_MENU
}

@Composable
fun LabelRoute(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onDrawerOpen: () -> Unit,
    viewModel: LabelViewModel = hiltViewModel()
) {
    val labelName by viewModel.labelName.collectAsStateWithLifecycle("")
    val noteViewState by viewModel.noteViewState.collectAsStateWithLifecycle()
    val contextMenuState by viewModel.contextMenuState.collectAsStateWithLifecycle()

    LabelScreen(
        labelName = labelName,
        noteViewState = noteViewState,
        contextMenuState = contextMenuState,
        onClickRenameLabel = {
            viewModel.toggleContextMenuState()

        },
        onClickDeleteLabel = {
            viewModel.toggleContextMenuState()
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
        }
    )
}

@Composable
fun LabelScreen(
    labelName: String,
    noteViewState: NoteView,
    contextMenuState: Boolean,
    onClickRenameLabel: () -> Unit,
    onClickDeleteLabel: () -> Unit,
    onClickLabelAppBarItem: (item: LabelAppBarItem) -> Unit
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
        EmptyNoteList(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            text = "No notes with this label yet",
            icon = VnIcons.label
        )
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