package com.openai.voicenote.core.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.openai.voicenote.core.designsystem.theme.VnTheme

@Composable
fun ConfirmationDialog(
    heading: String,
    description: String,
    confirmButtonText: String,
    dismissButtonText: String,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismissClick() },
        confirmButton = {
            TextButton(onClick = { onConfirmClick() }) {
                Text(
                    text = confirmButtonText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissClick() }) {
                Text(
                    text = dismissButtonText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        title = {
            Text(
                text = heading,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        text = {
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    )
}

@Preview
@Composable
fun ConfirmationDialogPreview() {
    VnTheme {
        Surface {
            ConfirmationDialog(
                heading = "Delete label?",
                description = "We'll delete this label and remove it from all of your keep notes. Your notes won't be deleted.",
                confirmButtonText = "Delete",
                dismissButtonText = "Cancel",
                onConfirmClick = {  },
                onDismissClick = {  }
            )
        }
    }
}