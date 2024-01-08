package com.openai.voicenote.navigation

import androidx.annotation.DrawableRes
import com.openai.voicenote.core.designsystem.icon.VnIcons

enum class DrawerDestination(
    @DrawableRes val icon: Int,
    val text: String
) {
    NOTES(
        icon = VnIcons.note,
        text = "Notes"
    ),
    CREATE_NEW_LABEL(
        icon = VnIcons.add,
        text = "Create new label"
    ),
    ARCHIVE(
        icon = VnIcons.archive,
        text = "Archive"
    ),
    TRASH(
        icon = VnIcons.delete,
        text = "Trash"
    ),
    SETTINGS(
        icon = VnIcons.settings,
        text = "Settings"
    ),
    HELP_AND_FEEDBACK(
        icon = VnIcons.help,
        text = "Help & feedback"
    )
}