package com.openai.voicenote.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.openai.voicenote.DrawerMenuOption
import com.openai.voicenote.R
import com.openai.voicenote.model.DrawerMenu
import kotlinx.coroutines.launch

@Composable
fun AppNavDrawer(
    items: List<DrawerMenu>,
    selectedIndex: Int,
    drawerState: DrawerState,
    onClick: (DrawerMenuOption) -> Unit
) {
    val scope = rememberCoroutineScope()

    ModalDrawerSheet(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(topEnd = 48.dp, bottomEnd = 64.dp))
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.note_app),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
        )
        items.forEachIndexed { index, drawerMenu ->
            NavigationDrawerItem(
                label = {
                    Text(
                        text = stringResource(id = drawerMenu.title),
                        style = MaterialTheme.typography.titleSmall,
                        color = getColor(index == selectedIndex)
                    )
                },
                selected = selectedIndex == index,
                onClick = {
                    onClick(drawerMenu.menuOption)
                    scope.launch {
                        drawerState.close()
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = drawerMenu.icon),
                        contentDescription = stringResource(id = drawerMenu.title),
                        tint = getColor(active = selectedIndex == index)
                    )
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }

}

@Composable
fun getColor(active: Boolean): Color {
    if (active) {
        return MaterialTheme.colorScheme.surfaceTint
    }
    return MaterialTheme.colorScheme.onSurface
}