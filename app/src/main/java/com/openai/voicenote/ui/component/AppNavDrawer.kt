package com.openai.voicenote.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openai.voicenote.R
import com.openai.voicenote.model.DrawerMenu
import com.openai.voicenote.model.Label
import kotlinx.coroutines.launch

@Composable
fun AppNavDrawer(
    items: List<DrawerMenu>,
    labelItems: List<Label>,
    selectedIndex: Int,
    selectedLabelIndex: Int,
    drawerState: DrawerState,
    onClick: (menuOptionOrdinal: Int, labelOptionOrdinal: Int) -> Unit
) {
    val scope = rememberCoroutineScope()

    ModalDrawerSheet(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(topEnd = 48.dp, bottomEnd = 64.dp))
    ) {
        LazyColumn() {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                Text(
                    text = stringResource(id = R.string.note_app),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
                )
            }
            items(items) { drawerMenu ->
                if (drawerMenu.title == R.string.create_new_label && labelItems.isNotEmpty()) {
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
                    labelItems.forEachIndexed { index, item ->
                        DrawerItem(
                            title = item.labelName,
                            drawableId = R.drawable.label_24,
                            currentOrdinal = index,
                            selectedIndex = selectedLabelIndex
                        ) {
                            onClick(-1, index)
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    }
                    DrawerItem(
                        title = stringResource(id = drawerMenu.title),
                        drawableId = drawerMenu.icon,
                        currentOrdinal = drawerMenu.menuOption.ordinal,
                        selectedIndex = selectedIndex,
                    ) {
                        onClick(drawerMenu.menuOption.ordinal, -1)
                        scope.launch {
                            drawerState.close()
                        }
                    }
                    DividerWithSpacer()
                }
                else {
                    DrawerItem(
                        title = stringResource(id = drawerMenu.title),
                        drawableId = drawerMenu.icon,
                        currentOrdinal = drawerMenu.menuOption.ordinal,
                        selectedIndex = selectedIndex,
                    ) {
                        onClick(drawerMenu.menuOption.ordinal, -1)
                        scope.launch {
                            drawerState.close()
                        }
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
    currentOrdinal: Int,
    selectedIndex: Int,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.W900,
                color = getColor(currentOrdinal == selectedIndex)
            )
        },
        selected = selectedIndex == currentOrdinal,
        onClick = {
            onClick()
        },
        icon = {
            Icon(
                painter = painterResource(id = drawableId),
                contentDescription = title,
                tint = getColor(active = selectedIndex == currentOrdinal)
            )
        },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
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

@Composable
fun getColor(active: Boolean): Color {
    if (active) {
        return MaterialTheme.colorScheme.primary
    }
    return MaterialTheme.colorScheme.onSurface
}