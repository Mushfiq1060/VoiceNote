package com.openai.voicenote.core.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openai.voicenote.core.designsystem.icon.VnIcons
import com.openai.voicenote.core.designsystem.theme.VnTheme

@Composable
fun EmptyNoteList(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes icon: Int,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(84.dp),
            painter = painterResource(id = icon),
            contentDescription = "empty icon",
            tint = MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = Modifier.size(18.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyNoteListPreview() {
    VnTheme {
        Surface {
            EmptyNoteList(
                modifier = Modifier.padding(36.dp),
                text = "Notes you add appear here",
                icon = VnIcons.note
            )
        }
    }
}