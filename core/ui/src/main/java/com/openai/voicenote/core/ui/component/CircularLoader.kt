package com.openai.voicenote.core.ui.component

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.openai.voicenote.core.designsystem.theme.VnTheme

@Composable
fun CircularLoader(
    color: Color,
    strokeWidth: Dp
) {
    CircularProgressIndicator(
        modifier = Modifier.drawBehind {
            drawCircle(
                Color.Transparent,
                radius = size.width / 2 - strokeWidth.toPx() / 2,
                style = Stroke(strokeWidth.toPx())
            )
        },
        color = color,
        strokeWidth = strokeWidth
    )
}

@Preview(showBackground = true)
@Composable
fun CircularLoaderPreview() {
    VnTheme {
        Surface {
            CircularLoader(
                color = MaterialTheme.colorScheme.onTertiary,
                strokeWidth = 5.dp
            )
        }
    }
}