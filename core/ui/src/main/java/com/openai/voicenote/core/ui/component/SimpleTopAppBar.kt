package com.openai.voicenote.core.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openai.voicenote.core.designsystem.icon.VnIcons
import com.openai.voicenote.core.designsystem.theme.VnTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopAppBar(
    @DrawableRes navigationIcon: Int,
    title: String,
    onClickNavigationIcon: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        navigationIcon = {
            IconButton(onClick = { onClickNavigationIcon() }) {
                Icon(
                    painter = painterResource(id = VnIcons.arrowBack),
                    contentDescription = "back button",
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    )
}

@Preview
@Composable
fun SimpleTopAppBarPreview() {
    VnTheme {
        Surface {
            SimpleTopAppBar(
                navigationIcon = VnIcons.arrowBack,
                title = "Edit Labels",
                onClickNavigationIcon = {  }
            )
        }
    }
}
