package com.opanai.voicenote.feature.notelabel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.openai.voicenote.core.designsystem.icon.VnIcons
import com.openai.voicenote.core.designsystem.theme.VnTheme
import com.openai.voicenote.core.model.LabelResource
import com.openai.voicenote.core.ui.component.CircularLoader
import com.openai.voicenote.core.ui.component.SimpleTopAppBar

@Composable
fun NoteLabelRoute(
    onBackClick: () -> Unit,
    viewModel: NoteLabelViewModel = hiltViewModel()
) {
    val labelUiState by viewModel.labelUiState.collectAsStateWithLifecycle()

    NoteLabelScreen(
        onBackClick = onBackClick,
        labelUiState = labelUiState,
        onCheckClick = viewModel::onCheckCLick
    )
}

@Composable
internal fun NoteLabelScreen(
    onBackClick: () -> Unit,
    labelUiState: NoteLabelUiState,
    onCheckClick: (Long, Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            SimpleTopAppBar(
                navigationIcon = VnIcons.arrowBack,
                title = stringResource(id = R.string.feature_notelabel_add_label),
                onClickNavigationIcon = onBackClick
            )
        }
    ) { paddingValues ->
        when (labelUiState) {
            is NoteLabelUiState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularLoader(
                        color = MaterialTheme.colorScheme.tertiary,
                        strokeWidth = 5.dp
                    )
                }
            }
            is NoteLabelUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    items(labelUiState.list) { uiState ->
                        LabelCheckRow(
                            label = uiState.label,
                            checkBoxStatus = uiState.checkStatus,
                            onCheckClick = onCheckClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun LabelCheckRow(
    label: LabelResource,
    checkBoxStatus: ToggleableState,
    onCheckClick: (Long, Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = VnIcons.label),
            contentDescription = "label",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .size(28.dp)
                .weight(0.15f)
        )
        Text(
            text = label.labelName,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(0.70f)
        )
        TriStateCheckbox(
            state = checkBoxStatus,
            onClick = {
                if (checkBoxStatus == ToggleableState.On) {
                    onCheckClick(label.labelId!!,false)
                } else {
                    onCheckClick(label.labelId!!, true)
                }
            },
            modifier = Modifier.weight(0.15f)
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun LazyCheckRowPreview() {
    VnTheme {
        Surface {
            LabelCheckRow(
                label = LabelResource(null, "One"),
                checkBoxStatus = ToggleableState.Indeterminate,
                onCheckClick = { _, _ -> }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun NoteLabelScreenPreview() {
    VnTheme {
        Surface {
            NoteLabelScreen(
                onBackClick = {},
                labelUiState = previewList,
                onCheckClick = { _, _ -> }
            )
        }
    }
}

val previewList = NoteLabelUiState.Success(
    list = listOf(
        UiState(
            label = LabelResource(
                labelId = 1,
                labelName = "One"
            ),
            checkStatus = ToggleableState.Off
        ),
        UiState(
            label = LabelResource(
                labelId = 2,
                labelName = "Two"
            ),
            checkStatus = ToggleableState.Indeterminate
        ),
        UiState(
            label = LabelResource(
                labelId = 3,
                labelName = "Three"
            ),
            checkStatus = ToggleableState.On
        ),
    )
)

