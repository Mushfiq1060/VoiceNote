package com.openai.voicenote.core.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openai.voicenote.core.designsystem.icon.VnIcons
import com.openai.voicenote.core.designsystem.theme.VnTheme

enum class SelectedTopAppBarItem {
    CANCEL,
    TOGGLE_PIN,
    DRAW,
    LABEL,
    CONTEXT_MENU_OPEN,
    CONTEXT_MENU_CLOSE,
    TOGGLE_ARCHIVE,
    DELETE,
    MAKE_A_COPY
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedTopAppBar(
    selectedCount: Int,
    isSelectedOtherNote: Boolean,
    isContextMenuOpen: Boolean,
    archiveStatus: Boolean,
    onClick: (type: SelectedTopAppBarItem) -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        title = {
            Text(
                text = "$selectedCount",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    onClick(SelectedTopAppBarItem.CANCEL)
                }
            ) {
                Icon(
                    painter = painterResource(id = VnIcons.close),
                    contentDescription = "close",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    onClick(SelectedTopAppBarItem.TOGGLE_PIN)
                }
            ) {
                Icon(
                    painter = painterResource(id = checkPinStatus(isSelectedOtherNote)),
                    contentDescription = "toggle pin",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(28.dp)
                )
            }
            IconButton(
                onClick = {
                    onClick(SelectedTopAppBarItem.DRAW)
                }
            ) {
                Icon(
                    painter = painterResource(id = VnIcons.colorPalette),
                    contentDescription = "draw",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(28.dp)
                )
            }
            IconButton(
                onClick = {
                    onClick(SelectedTopAppBarItem.LABEL)
                }
            ) {
                Icon(
                    painter = painterResource(id = VnIcons.label),
                    contentDescription = "label",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(28.dp)
                )
            }
            IconButton(
                onClick = {
                    onClick(SelectedTopAppBarItem.CONTEXT_MENU_OPEN)
                }
            ) {
                Icon(
                    painter = painterResource(id = VnIcons.moreVert),
                    contentDescription = "context menu",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(28.dp)
                )
                DropdownMenu(
                    expanded = isContextMenuOpen,
                    onDismissRequest = {
                        onClick(SelectedTopAppBarItem.CONTEXT_MENU_CLOSE)
                    }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = if (archiveStatus) "Unarchive" else "Archive",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            onClick(SelectedTopAppBarItem.TOGGLE_ARCHIVE)
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Delete",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            onClick(SelectedTopAppBarItem.DELETE)
                        }
                    )
                    if (selectedCount == 1) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Make a copy",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            onClick = {
                                onClick(SelectedTopAppBarItem.MAKE_A_COPY)
                            }
                        )
                    }
                }
            }
        }
    )
}

fun checkPinStatus(pinState: Boolean): Int {
    if (!pinState) {
        return VnIcons.filledPin
    }
    return VnIcons.pin
}

@Preview(showBackground = true)
@Composable
fun SelectedTopAppBarPreview() {
    VnTheme {
        Surface {
            SelectedTopAppBar(
                selectedCount = 1,
                isSelectedOtherNote = true,
                isContextMenuOpen = false,
                archiveStatus = false,
                onClick = {

                }
            )
        }
    }
}