package com.opanai.voicenote.feature.notelabel

import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.openai.voicenote.core.designsystem.icon.VnIcons
import com.openai.voicenote.core.designsystem.theme.VnTheme
import com.openai.voicenote.core.model.LabelResource
import com.openai.voicenote.core.ui.component.SimpleTopAppBar

@Composable
fun NoteLabelRoute(
    onBackClick: () -> Unit,
    viewModel: NoteLabelViewModel = hiltViewModel()
) {
    NoteLabelScreen(
        labelList = listOf(),
    )
}

@Composable
fun NoteLabelScreen(
    labelList: List<LabelResource>,
) {
    Scaffold(
        topBar = {
            SimpleTopAppBar(
                navigationIcon = VnIcons.arrowBack,
                title = "Add label",
                onClickNavigationIcon = {}
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            items(labelList) { label ->
                LabelCheckRow(
                    labelName = label.labelName,
                    checkBoxStatus = ToggleableState.On,
                    onCheckClick = {}
                )
            }
        }
    }
}

@Composable
fun LabelCheckRow(
    labelName: String,
    checkBoxStatus: ToggleableState,
    onCheckClick: (status: ToggleableState) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
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
            text = labelName,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(0.70f)
        )
        TriStateCheckbox(
            state = checkBoxStatus,
            onClick = {
                if (checkBoxStatus == ToggleableState.On) {
                    onCheckClick(ToggleableState.Off)
                } else {
                    onCheckClick(ToggleableState.On)
                }
            },
            modifier = Modifier.weight(0.15f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LazyCheckRowPreview() {
    VnTheme {
        Surface {
            LabelCheckRow(
                labelName = "One",
                checkBoxStatus = ToggleableState.Indeterminate,
                onCheckClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteLabelScreenPreview() {
    VnTheme {
        Surface {
            NoteLabelScreen(
                labelList = previewLabelList
            )
        }
    }
}

val previewLabelList = listOf<LabelResource>(
    LabelResource(
        labelId = 1,
        labelName = "One"
    ),
    LabelResource(
        labelId = 2,
        labelName = "Two"
    ),
    LabelResource(
        labelId = 3,
        labelName = "Three"
    ),
    LabelResource(
        labelId = 4,
        labelName = "Four"
    ),
    LabelResource(
        labelId = 5,
        labelName = "Five"
    ),
    LabelResource(
        labelId = 6,
        labelName = "Six"
    ),
)

