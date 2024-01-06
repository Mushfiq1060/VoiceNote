package com.openai.voicenote.core.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.openai.voicenote.core.designsystem.icon.VnColor
import com.openai.voicenote.core.designsystem.icon.VnImage
import com.openai.voicenote.core.designsystem.theme.VnTheme
import com.openai.voicenote.core.model.NoteResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteCard(
    note: NoteResource,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .border(
                shape = MaterialTheme.shapes.large,
                border = BorderStroke(getBorderWidth(isSelected), getBorderColor(isSelected)),
            )
            .background(Color(VnColor.bgColorList[note.backgroundColor].colorCode))
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { onLongClick() }
            )
    ) {
        if (note.backgroundImage != VnImage.bgImageList[0].id) {
            Image(
                modifier = Modifier
                    .matchParentSize()
                    .clip(MaterialTheme.shapes.large),
                painter = painterResource(
                    id = VnImage.bgImageList[note.backgroundImage].drawableId
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            if (note.title.isNotEmpty()) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.W900,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(
                text = note.description,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

fun getBorderWidth(selected: Boolean): Dp {
    if (!selected) {
        return 2.dp
    }
    return 3.dp
}

@Composable
fun getBorderColor(selected: Boolean): Color {
    if (!selected) {
        return MaterialTheme.colorScheme.outlineVariant
    }
    return MaterialTheme.colorScheme.primary
}

@Preview(showBackground = true)
@Composable
fun NoteCardPreview() {
    VnTheme {
        Surface(modifier = Modifier.padding(32.dp)) {
            NoteCard(
                note = previewNote,
                isSelected = false,
                onClick = {  },
                onLongClick = {  }
            )
        }
    }
}

val previewNote = NoteResource(
    noteId = -1,
    title = "One",
    description = "Something is wrong.\nWhy you are not help me?\nOne day I will rise again.",
    editTime = 263566L,
    pin = true,
    archive = false,
    backgroundColor = 0,
    backgroundImage = 2
)