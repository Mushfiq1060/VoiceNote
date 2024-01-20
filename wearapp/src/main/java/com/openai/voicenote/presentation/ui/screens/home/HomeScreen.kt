package com.openai.voicenote.presentation.ui.screens.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.openai.voicenote.core.designsystem.icon.VnIcons
import com.openai.voicenote.presentation.theme.VnWearTheme

data class HomeContent(
    val contentName: String,
    @DrawableRes val contentIcon: Int,
    val type: String
)

val contentList = listOf(
    HomeContent(
        contentName = "Add Note",
        contentIcon = VnIcons.add,
        type = "ADD_NOTE"
    ),
    HomeContent(
        contentName = "Pinned Notes",
        contentIcon = VnIcons.filledPin,
        type = "PIN_NOTES"
    ),
    HomeContent(
        contentName = "Other Notes",
        contentIcon = VnIcons.pin,
        type = "OTHER_NOTES"
    ),
    HomeContent(
        contentName = "Archive Notes",
        contentIcon = VnIcons.archive,
        type = "ARCHIVE_NOTES"
    ),
    HomeContent(
        contentName = "Trash",
        contentIcon = VnIcons.delete,
        type = "TRASH_NOTES"
    ),
    HomeContent(
        contentName = "Help & feedback",
        contentIcon = VnIcons.help,
        type = "HELP"
    )
)

@Composable
fun HomeRoute(
    onNavigateToAddNote: () -> Unit,
    onNavigateToViewNote: (String) -> Unit,
    onNavigateToHelp: () -> Unit
) {
    HomeScreen(
        contentList = contentList,
        onNavigateToAddNote = onNavigateToAddNote,
        onNavigateToViewNote = onNavigateToViewNote,
        onNavigateToHelp = onNavigateToHelp
    )
}

@Composable
internal fun HomeScreen(
    contentList: List<HomeContent>,
    onNavigateToAddNote: () -> Unit,
    onNavigateToViewNote: (String) -> Unit,
    onNavigateToHelp: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        timeText = { TimeText() }
    ) {
        ScalingLazyColumn() {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Note App",
                        style = MaterialTheme.typography.display2
                    )
                }
            }
            itemsIndexed(contentList) { index, content ->
                Button(
                    modifier = Modifier
                        .fillMaxSize(),
                    onClick = {
                        when (index) {
                            0 -> { onNavigateToAddNote() }
                            5 -> { onNavigateToHelp() }
                            else -> { onNavigateToViewNote(content.type) }
                        }
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Spacer(modifier = Modifier.width(16.dp))
                        Box(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colors.onPrimary,
                                    shape = CircleShape
                                )
                                .size(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = content.contentIcon),
                                contentDescription = content.contentName,
                                tint = MaterialTheme.colors.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = content.contentName,
                            style = MaterialTheme.typography.display2,
                            color = MaterialTheme.colors.onBackground
                        )
                    }
                }
            }
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    VnWearTheme {
        HomeScreen(
            contentList = contentList,
            onNavigateToAddNote = {},
            onNavigateToViewNote = {},
            onNavigateToHelp = {}
        )
    }
}