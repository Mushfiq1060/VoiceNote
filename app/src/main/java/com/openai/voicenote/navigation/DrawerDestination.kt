package com.openai.voicenote.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.openai.voicenote.R
import com.openai.voicenote.core.designsystem.icon.VnIcons

enum class DrawerDestination(
    @DrawableRes val icon: Int,
    @StringRes val textId: Int
) {
    NOTES(
        icon = VnIcons.note,
        textId = R.string.notes
    ),
    CREATE_NEW_LABEL(
        icon = VnIcons.add,
        textId = R.string.create_new_label
    ),
    ARCHIVE(
        icon = VnIcons.archive,
        textId = R.string.archive
    ),
    TRASH(
        icon = VnIcons.delete,
        textId = R.string.trash
    ),
    SETTINGS(
        icon = VnIcons.settings,
        textId = R.string.settings
    ),
    HELP_AND_FEEDBACK(
        icon = VnIcons.help,
        textId = R.string.help_and_feedback
    )
}