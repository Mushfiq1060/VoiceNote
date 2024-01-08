package com.openai.voicenote.feature.labeledit

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openai.voicenote.core.designsystem.icon.VnIcons
import com.openai.voicenote.core.designsystem.theme.VnTheme
import com.openai.voicenote.core.model.LabelResource
import com.openai.voicenote.core.ui.component.SimpleTopAppBar

@Composable
fun LabelEditRoute(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: LabelEditViewModel = hiltViewModel()
) {
    val labelEditUiState by viewModel.labelEditUiState.collectAsStateWithLifecycle()

    LabelEditScreen(
        labelEditUiState = labelEditUiState,
        onBackClick = { onBackClick() }
    )

}

@Composable
fun LabelEditScreen(
    labelEditUiState: LabelEditUiState,
    onBackClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SimpleTopAppBar(
                navigationIcon = VnIcons.arrowBack,
                title = "Edit Labels",
                onClickNavigationIcon = { onBackClick() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            LabelTextField(
                value = "",
                enableEditing = labelEditUiState.enableEditing,
                onTextChange = {  },
                prefixIcon = if (labelEditUiState.enableEditing) VnIcons.close else VnIcons.add,
                suffixIcon = VnIcons.check,
                placeholderText = "Create a label",
                onPrefixIconClick = { /*TODO*/ },
                onSuffixIconClick = { /*TODO*/ }
            )
            LazyColumn() {
                itemsIndexed(
                    labelEditUiState.labelList,
                    key = { _, label -> label.labelId!! },
                    contentType = { _, _ -> "label item" }
                ) { index, label ->
                    LabelCard(
                        index = index,
                        label = label,
                        onPrefixIconClick = {},
                        onSuffixIconClick = {}
                    )
                }
            }
        }
    }
}

@Composable
fun LabelCard(
    index: Int,
    label: LabelResource,
    onPrefixIconClick: () -> Unit,
    onSuffixIconClick: () -> Unit
) {
    Row() {
        IconButton(
            modifier = Modifier.weight(0.1f),
            onClick = { onPrefixIconClick() }
        ) {
            Icon(
                painter = painterResource(id = VnIcons.label),
                contentDescription = "prefix icon",
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        Text(
            modifier = Modifier
                .weight(0.8f)
                .padding(start = 24.dp, end = 24.dp)
                .align(Alignment.CenterVertically),
            text = label.labelName,
            style = MaterialTheme.typography.titleMedium
        )
        IconButton(
            modifier = Modifier.weight(0.1f),
            onClick = { onSuffixIconClick() }
        ) {
            Icon(
                painter = painterResource(id = VnIcons.edit),
                contentDescription = "suffix icon",
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun LabelTextField(
    value: String,
    enableEditing: Boolean,
    onTextChange: (text: String) -> Unit,
    @DrawableRes prefixIcon: Int,
    @DrawableRes suffixIcon: Int,
    placeholderText: String,
    onPrefixIconClick: () -> Unit,
    onSuffixIconClick: () -> Unit
) {
    val borderColor = MaterialTheme.colorScheme.outline
    BasicTextField(
        value = value,
        onValueChange = { onTextChange(it) },
        singleLine = true,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .drawBehind {
                        val strokeWidth = 2.dp.toPx()
                        val yTop = 0f
                        val yBottom = size.height - strokeWidth
                        drawLine(
                            color = borderColor,
                            start = Offset(0f, yTop),
                            end = Offset(size.width, yTop),
                            strokeWidth = strokeWidth
                        )
                        drawLine(
                            color = borderColor,
                            start = Offset(0f, yBottom),
                            end = Offset(size.width, yBottom),
                            strokeWidth = strokeWidth
                        )
                    }
            ) {
                IconButton(
                    modifier = Modifier.weight(0.1f),
                    onClick = { onPrefixIconClick() }
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
                if (enableEditing) {
                    IconButton(
                        modifier = Modifier.weight(0.1f),
                        onClick = { onSuffixIconClick() }
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
fun Placeholder(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.outline
    )
}

@Preview(showBackground = true)
@Composable
fun LabelTextFieldPreview() {
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
                onPrefixIconClick = {},
                onSuffixIconClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LabelEditScreenPreview() {
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
                    enableEditing = true
                )
            )
        }
    }
}