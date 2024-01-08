package com.openai.voicenote.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openai.voicenote.core.designsystem.icon.VnIcons
import com.openai.voicenote.core.designsystem.theme.VnTheme
import com.openai.voicenote.core.model.LabelResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavDrawer(
    destinations: List<DrawerDestination>,
    labelItems: List<LabelResource>,
    currentDrawerDestination: DrawerDestination,
    selectedLabelIndex: Int,
    drawerState: DrawerState,
    coroutineScope: CoroutineScope,
    onNavigateToDrawerDestination: (DrawerDestination) -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(topEnd = 48.dp, bottomEnd = 48.dp))
    ) {
        LazyColumn() {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                Text(
                    text = "Note App",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
                )
            }
            items(destinations) { items ->
                if (items.text == "Create new label" && labelItems.isNotEmpty()) {
                    DividerWithSpacer()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Labels",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(start = 32.dp)
                        )
                        Text(
                            text = "Edit",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(end = 32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.size(16.dp))
                    labelItems.forEachIndexed { index, labelResource ->
                        DrawerItem(
                            title = labelResource.labelName,
                            drawableId = VnIcons.label,
                            active = selectedLabelIndex == index
                        ) {
                            onNavigateToDrawerDestination(items) // must be passed level info
                            coroutineScope.launch { drawerState.close() }
                        }
                    }
                    DrawerItem(
                        title = items.text,
                        drawableId = items.icon,
                        active = items == currentDrawerDestination
                    ) {
                        onNavigateToDrawerDestination(items)
                        coroutineScope.launch { drawerState.close() }
                    }
                    DividerWithSpacer()
                }
                else {
                    DrawerItem(
                        title = items.text,
                        drawableId = items.icon,
                        active = items == currentDrawerDestination
                    ) {
                        onNavigateToDrawerDestination(items)
                        coroutineScope.launch { drawerState.close() }
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerItem(
    title: String,
    drawableId: Int,
    active: Boolean,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.W900,
                color = getColor(active)
            )
        },
        selected = active,
        onClick = {
            onClick()
        },
        icon = {
            Icon(
                painter = painterResource(id = drawableId),
                contentDescription = title,
                tint = getColor(active)
            )
        },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}

@Composable
fun getColor(active: Boolean): Color {
    if (active) {
        return MaterialTheme.colorScheme.primary
    }
    return MaterialTheme.colorScheme.onSurface
}
@Composable
fun DividerWithSpacer() {
    Spacer(modifier = Modifier.size(16.dp))
    Divider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.inversePrimary
    )
    Spacer(modifier = Modifier.size(16.dp))
}

@Preview(showBackground = true)
@Composable
fun NavDrawerPreview() {
    VnTheme {
        Surface {
            NavDrawer(
                destinations = DrawerDestination.entries,
                labelItems = labelResourcePreview,
                currentDrawerDestination = DrawerDestination.SETTINGS,
                selectedLabelIndex = 2,
                drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
                coroutineScope = rememberCoroutineScope(),
                onNavigateToDrawerDestination = {}
            )
        }
    }
}

val labelResourcePreview = listOf(
    LabelResource(labelId = 1L, labelName = "One"),
    LabelResource(labelId = 2L, labelName = "Two"),
    LabelResource(labelId = 3L, labelName = "Three"),
    LabelResource(labelId = 4L, labelName = "Four"),
)