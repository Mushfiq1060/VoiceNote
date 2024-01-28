package com.openai.voicenote.feature.labeledit

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openai.voicenote.core.designsystem.icon.VnIcons
import com.openai.voicenote.core.designsystem.theme.VnTheme
import com.openai.voicenote.core.model.LabelResource
import com.openai.voicenote.core.ui.component.ConfirmationDialog
import com.openai.voicenote.core.ui.component.SimpleTopAppBar

@Composable
fun LabelEditRoute(
    onBackClick: () -> Unit,
    viewModel: LabelEditViewModel = hiltViewModel()
) {
    val labelEditUiState by viewModel.labelEditUiState.collectAsStateWithLifecycle()
    val shouldShowDeleteDialog by viewModel.shouldShowDeleteDialog.collectAsStateWithLifecycle()

    LabelEditScreen(
        labelEditUiState = labelEditUiState,
        onBackClick = onBackClick,
        toggleEnableEditing = viewModel::toggleEnableEditing,
        onAddLabel = viewModel::addLabel,
        onUpdateLabel = viewModel::updateLabel,
        onChangeTextInCreateLabel = viewModel::onChangeTextInCreateLabel,
        onChangeUpdateLabelText = viewModel::onChangeUpdateLabelText,
        onClickLabel = viewModel::onChangeUpdateLabelIndex,
        shouldShowDeleteDialog = shouldShowDeleteDialog,
        onOpenDeleteDialog = viewModel::toggleDeleteDialog,
        onDialogConfirmClick = viewModel::deleteLabel,
        onDialogCancelClick = viewModel::toggleDeleteDialog
    )

}

@Composable
internal fun LabelEditScreen(
    labelEditUiState: LabelEditUiState,
    onBackClick: () -> Unit,
    toggleEnableEditing: () -> Unit,
    onAddLabel: () -> Unit,
    onUpdateLabel: (Int) -> Unit,
    onChangeTextInCreateLabel: (String) -> Unit,
    onChangeUpdateLabelText: (String) -> Unit,
    onClickLabel: (Int) -> Unit,
    shouldShowDeleteDialog: Boolean,
    onOpenDeleteDialog: () -> Unit,
    onDialogConfirmClick: () -> Unit,
    onDialogCancelClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SimpleTopAppBar(
                navigationIcon = VnIcons.arrowBack,
                title = stringResource(id = R.string.feature_labeledit_edit_labels),
                onClickNavigationIcon = onBackClick
            )
        }
    ) { paddingValues ->
        if (shouldShowDeleteDialog) {
            ConfirmationDialog(
                heading = stringResource(id = R.string.feature_labeledit_delete_label),
                description = stringResource(id = R.string.feature_labeledit_warning),
                confirmButtonText = stringResource(id = R.string.feature_labeledit_delete),
                dismissButtonText = stringResource(id = R.string.feature_labeledit_cancel),
                onConfirmClick = onDialogConfirmClick,
                onDismissClick = onDialogCancelClick
            )
        }
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(1.dp))
            LabelTextField(
                value = labelEditUiState.createLabelText,
                enableEditing = labelEditUiState.enableEditing,
                onTextChange = onChangeTextInCreateLabel,
                prefixIcon = if (labelEditUiState.enableEditing) VnIcons.close else VnIcons.add,
                suffixIcon = VnIcons.check,
                placeholderText = stringResource(id = R.string.feature_labeledit_create_a_label),
                isSuffixIconVisible = labelEditUiState.enableEditing,
                onPrefixIconClick = toggleEnableEditing,
                onSuffixIconClick = onAddLabel
            )
            Spacer(modifier = Modifier.height(2.dp))
            LazyColumn {
                itemsIndexed(
                    labelEditUiState.labelList,
                    key = { _, label -> label.labelId!! },
                    contentType = { _, _ -> "label item" }
                ) { index, label ->
                    LabelTextField(
                        value = if (labelEditUiState.updateLabelIndex == index) labelEditUiState.updateLabelText else label.labelName,
                        enableEditing = labelEditUiState.updateLabelIndex == index,
                        onTextChange = onChangeUpdateLabelText,
                        prefixIcon = if (labelEditUiState.updateLabelIndex == index) VnIcons.delete else VnIcons.label,
                        suffixIcon = if (labelEditUiState.updateLabelIndex == index) VnIcons.check else VnIcons.edit,
                        placeholderText = "",
                        prefixIconEnable = labelEditUiState.updateLabelIndex == index,
                        isSuffixIconVisible = true,
                        onPrefixIconClick = onOpenDeleteDialog,
                        onSuffixIconClick = {
                            if (labelEditUiState.updateLabelIndex == index) {
                                onUpdateLabel(index)
                            } else {
                                onClickLabel(index)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
internal fun LabelTextField(
    value: String,
    enableEditing: Boolean,
    onTextChange: (String) -> Unit,
    @DrawableRes prefixIcon: Int,
    @DrawableRes suffixIcon: Int,
    placeholderText: String,
    prefixIconEnable: Boolean = true,
    isSuffixIconVisible: Boolean,
    onPrefixIconClick: () -> Unit,
    onSuffixIconClick: () -> Unit
) {
    val borderColor = MaterialTheme.colorScheme.outline
    BasicTextField(
        value = value,
        onValueChange = onTextChange,
        textStyle = MaterialTheme.typography.bodyMedium,
        enabled = enableEditing,
        singleLine = true,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .drawBehind {
                        if (enableEditing) {
                            val strokeWidth = 2f
                            val x = size.width - strokeWidth
                            val y = size.height - strokeWidth
                            /** top line **/
                            drawLine(
                                color = borderColor,
                                start = Offset(0f, 0f), /** (0,0) at top-left point of the box **/
                                end = Offset(x, 0f), /** top-right point of the box **/
                                strokeWidth = strokeWidth
                            )
                            /** bottom line **/
                            drawLine(
                                color = borderColor,
                                start = Offset(0f, y), /** bottom-left point of the box **/
                                end = Offset(x, y), /** bottom-right point of the box **/
                                strokeWidth = strokeWidth
                            )
                        }
                    }
                    .padding(top = 8.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
            ) {
                IconButton(
                    modifier = Modifier.weight(0.1f),
                    onClick = onPrefixIconClick,
                    enabled = prefixIconEnable
                ) {
                    Icon(
                        painter = painterResource(id = prefixIcon),
                        contentDescription = "prefix icon",
                        modifier = Modifier.size(28.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(if (enableEditing) 0.8f else 0.9f)
                        .padding(start = 24.dp, end = 24.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    if (value.isEmpty()) {
                        Placeholder(placeholderText)
                    }
                    innerTextField()
                }
                if (isSuffixIconVisible) {
                    IconButton(
                        modifier = Modifier.weight(0.1f),
                        onClick = onSuffixIconClick
                    ) {
                        Icon(
                            painter = painterResource(id = suffixIcon),
                            contentDescription = "suffix icon",
                            modifier = Modifier.size(28.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

            }
        }
    )
}

@Composable
internal fun Placeholder(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.outline
    )
}

@Preview(showBackground = true)
@Composable
internal fun LabelTextFieldPreview() {
    VnTheme {
        Surface(
            modifier = Modifier.padding(16.dp)
        ) {
            LabelTextField(
                value = "",
                enableEditing = true,
                onTextChange = {},
                prefixIcon = VnIcons.close,
                suffixIcon = VnIcons.check,
                placeholderText = "Create a label",
                isSuffixIconVisible = false,
                onPrefixIconClick = {},
                onSuffixIconClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun LabelEditScreenPreview() {
    VnTheme {
        Surface {
            LabelEditScreen(
                onBackClick = {},
                labelEditUiState = LabelEditUiState(
                    labelList = listOf(
                        LabelResource(labelId = 1, labelName = "One"),
                        LabelResource(labelId = 2, labelName = "Two"),
                        LabelResource(labelId = 3, labelName = "Three"),
                        LabelResource(labelId = 4, labelName = "Four"),
                        LabelResource(labelId = 5, labelName = "Five"),
                    ),
                    enableEditing = true,
                    createLabelText = "",
                    updateLabelText = "",
                    updateLabelIndex = -1
                ),
                toggleEnableEditing = {},
                onAddLabel = {},
                onUpdateLabel = {},
                onChangeTextInCreateLabel = {},
                onChangeUpdateLabelText = {},
                onClickLabel = {},
                shouldShowDeleteDialog = false,
                onOpenDeleteDialog = { },
                onDialogConfirmClick = { },
                onDialogCancelClick = {  }
            )
        }
    }
}