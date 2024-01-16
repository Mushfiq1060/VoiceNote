package com.openai.voicenote.core.ui.component

import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        sheetState = sheetState,
        dragHandle = null,
        shape = BottomSheetDefaults.HiddenShape,
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        onDismissRequest = { onDismiss() }
    ) {
        content()
    }
}