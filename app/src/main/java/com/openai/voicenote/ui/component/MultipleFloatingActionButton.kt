package com.openai.voicenote.ui.component

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.openai.voicenote.model.FabItem

enum class MultiFabState {
    EXPANDED,
    COLLAPSED
}

enum class SubFabType {
    TEXT,
    VOICE
}

@Composable
fun MultiFloatingActionButton(
    items : List<FabItem>,
    currentState : MultiFabState,
    onFabClicked : (MultiFabState) -> Unit,
    onSubFabClicked : (SubFabType) -> Unit
) {
    val transition = updateTransition(
        targetState = currentState,
        label = "transition"
    )
    val rotation by transition.animateFloat(
        label = "rotate"
    ) {
        if (it == MultiFabState.EXPANDED) 45f else 0f
    }
    Box() {
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
                    imageVector = Icons.Default.Add,
                    modifier = Modifier
                        .size(48.dp)
                        .rotate(rotation),
                    contentDescription = "FAB"
                )
            }
        }
    }
}

@Composable
fun SmallFloatingActionButtonRow(
    item : FabItem,
    transition : Transition<MultiFabState>,
    onClick : (SubFabType) -> Unit
) {
    val alpha by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 1000)
        },
        label = "alpha"
    ) {
        if (it == MultiFabState.EXPANDED) 1f else 0f
    }
    val scale by transition.animateFloat(label = "scale") {
        if (it == MultiFabState.EXPANDED) 1f else 0f
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .alpha(animateFloatAsState(targetValue = alpha).value)
            .scale(animateFloatAsState(targetValue = scale).value)
    ) {
        Text(
            text = item.label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
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
                painter = item.icon,
                contentDescription = item.label
            )
        }
    }

}