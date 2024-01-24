package com.openai.voicenote.core.ui.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openai.voicenote.core.designsystem.icon.VnIcons
import com.openai.voicenote.core.designsystem.theme.VnTheme
import com.openai.voicenote.core.ui.R

enum class FABState {
    EXPANDED,
    COLLAPSED
}

enum class SubFabType {
    TEXT,
    VOICE
}

data class FabItem(
    @DrawableRes val icon: Int,
    @StringRes val labelId: Int,
    val type: SubFabType
)

val subFABList = listOf(
    FabItem(
        icon = VnIcons.editNote,
        labelId = R.string.core_ui_add_text_note,
        type = SubFabType.TEXT
    ),
    FabItem(
        icon = VnIcons.mic,
        labelId = R.string.core_ui_add_voice_note,
        type = SubFabType.VOICE
    )
)

@Composable
fun FloatingButton(
    modifier: Modifier = Modifier,
    items: List<FabItem> = subFABList,
    currentState: FABState,
    onFabClicked: (FABState) -> Unit,
    onSubFabClicked: (SubFabType) -> Unit
) {
    val transition = updateTransition(
        targetState = currentState,
        label = "transition"
    )
    val rotation by transition.animateFloat(
        label = "rotate"
    ) {
        if (it == FABState.EXPANDED) 45f else 0f
    }
    Box(
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {
            items.forEach {
                SmallFloatingActionButtonRow(
                    item = it,
                    transition = transition
                ) { type ->
                    onSubFabClicked(type)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                onClick = {
                    onFabClicked(currentState)
                }
            ) {
                Icon(
                    painter = painterResource(id = VnIcons.add),
                    modifier = Modifier
                        .size(48.dp)
                        .rotate(rotation),
                    contentDescription = "FAB",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
fun SmallFloatingActionButtonRow(
    item: FabItem,
    transition: Transition<FABState>,
    onClick: (SubFabType) -> Unit
) {
    val alpha by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 1000)
        },
        label = "alpha"
    ) {
        if (it == FABState.EXPANDED) 1f else 0f
    }
    val scale by transition.animateFloat(label = "scale") {
        if (it == FABState.EXPANDED) 1f else 0f
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .alpha(animateFloatAsState(targetValue = alpha, label = "").value)
            .scale(animateFloatAsState(targetValue = scale, label = "").value)
    ) {
        Text(
            text = stringResource(id = item.labelId),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .clip(shape = MaterialTheme.shapes.large)
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .clickable(
                    onClick = { onClick(item.type) }
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)

        )
        Spacer(modifier = Modifier.width(8.dp))
        SmallFloatingActionButton(
            shape = CircleShape,
            modifier = Modifier.padding(4.dp),
            onClick = { onClick(item.type) },
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ) {
            Icon(
                painter = painterResource(id = item.icon),
                contentDescription = stringResource(id = item.labelId),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FloatingButtonPreview() {
    VnTheme {
        Surface {
            FloatingButton(
                modifier = Modifier.padding(36.dp),
                currentState = FABState.EXPANDED,
                onFabClicked = { } ,
                onSubFabClicked = { }
            )
        }
    }
}