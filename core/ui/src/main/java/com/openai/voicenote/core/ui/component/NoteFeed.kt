package com.openai.voicenote.core.ui.component

import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openai.voicenote.core.designsystem.theme.VnTheme
import com.openai.voicenote.core.model.NoteResource

enum class NoteType {
    PINNED,
    OTHERS
}

sealed interface NoteFeedUiState {

    data object Loading : NoteFeedUiState

    data class Success(
        val selectedPinNotes: MutableSet<Long>,
        val selectedOtherNotes: MutableSet<Long>,
        val pinnedNoteList: List<NoteResource>,
        val otherNoteList: List<NoteResource>
    ) : NoteFeedUiState

}

fun LazyStaggeredGridScope.header(
    content: @Composable LazyStaggeredGridItemScope.() -> Unit
) {
    item(
        span = StaggeredGridItemSpan.FullLine,
        content = content
    )
}

fun LazyStaggeredGridScope.noteFeed(
    noteItems: List<NoteResource>,
    selectedList: MutableSet<Long>,
    onClick: (note: NoteResource, noteId: Long) -> Unit,
    onLongClick: (noteId: Long) -> Unit
) {
    itemsIndexed(
        items = noteItems,
        key = { _, item -> item.noteId!! },
        contentType = { _, _ -> "note item" }
    ) { _, note ->
        NoteCard(
            note = note,
            isSelected = (selectedList.contains(note.noteId)),
            onClick = { onClick(note, note.noteId!!) },
            onLongClick = { onLongClick(note.noteId!!) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NoteFeedPreview() {
    VnTheme {
        Surface {
            LazyVerticalStaggeredGrid(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp),
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                flingBehavior = ScrollableDefaults.flingBehavior()
            ) {
                header {
                    Text(
                        text = "Pinned",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .paddingFromBaseline(bottom = 8.dp)
                            .padding(top = 16.dp, start = 16.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
                noteFeed(
                    noteItems = notePinnedPreviewList,
                    selectedList = mutableSetOf(),
                    onClick = { _, _ -> },
                    onLongClick = { _ -> }
                )
                header {
                    Text(
                        text = "Others",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .paddingFromBaseline(bottom = 8.dp)
                            .padding(top = 16.dp, start = 16.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
                noteFeed(
                    noteItems = noteOthersPreviewList,
                    selectedList = mutableSetOf(),
                    onClick = { _, _ -> },
                    onLongClick = { _ -> }
                )
            }
        }
    }
}

val notePinnedPreviewList = listOf<NoteResource>(
    NoteResource(
        noteId = 1,
        title = "One",
        description = "Something is wrong.\nWhy you are not help me?\nOne day I will rise again.\nAndroid developer.",
        editTime = 263566L,
        pin = true,
        archive = false,
        backgroundColor = Color.Transparent.toArgb(),
        backgroundImage = 2
    ),
    NoteResource(
        noteId = 2,
        title = "One",
        description = "Something is wrong.\nWhy you are not help me?\nOne day I will rise again.",
        editTime = 263566L,
        pin = true,
        archive = false,
        backgroundColor = Color.Transparent.toArgb(),
        backgroundImage = 4
    ),
)

val noteOthersPreviewList = listOf<NoteResource>(
    NoteResource(
        noteId = 3,
        title = "One",
        description = "Something is wrong.\nWhy you are not help me?\nOne day I will rise again.\nAndroid developer.",
        editTime = 263566L,
        pin = true,
        archive = false,
        backgroundColor = Color.Transparent.toArgb(),
        backgroundImage = 2
    ),
    NoteResource(
        noteId = 4,
        title = "One",
        description = "Something is wrong.\nWhy you are not help me?\nOne day I will rise again.",
        editTime = 263566L,
        pin = true,
        archive = false,
        backgroundColor = Color.Transparent.toArgb(),
        backgroundImage = 4
    ),
)