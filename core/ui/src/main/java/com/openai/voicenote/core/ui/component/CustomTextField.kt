package com.openai.voicenote.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openai.voicenote.core.designsystem.theme.VnTheme

@Composable
fun CustomTextField(
    value: String,
    onTextChange: (text: String) -> Unit,
    textStyle: TextStyle,
    singleLine: Boolean,
    maxLines: Int,
    placeholder: @Composable () -> Unit
) {
    BasicTextField(
        value = value,
        onValueChange = { onTextChange(it) },
        textStyle = textStyle,
        singleLine = singleLine,
        maxLines = maxLines,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                if (value.isEmpty()) {
                    placeholder()
                }
                innerTextField()
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CustomTextFieldPreview() {
    VnTheme {
        Surface {
            CustomTextField(
                value = "",
                onTextChange = {},
                textStyle = MaterialTheme.typography.titleLarge,
                singleLine = true,
                maxLines = 1
            ) {
                Text(
                    text = "Title",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}