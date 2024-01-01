package com.openai.voicenote.core.designsystem.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openai.voicenote.core.designsystem.icon.VnIcons
import com.openai.voicenote.core.designsystem.theme.VnTheme

@Composable
fun ViewToggle(
    gridEnable: Boolean,
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = { onClick(!gridEnable) },
        modifier = modifier.size(36.dp)
    ) {
        Icon(
            painter = painterResource(id = if(gridEnable) VnIcons.gridView else VnIcons.listView),
            contentDescription = "view",
            modifier = Modifier.size(36.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 200, heightDp = 200)
@Composable
fun ViewTogglePreview() {
    VnTheme {
        Surface {
            ViewToggle(
                gridEnable = false,
                onClick = {

                }
            )
        }
    }
}