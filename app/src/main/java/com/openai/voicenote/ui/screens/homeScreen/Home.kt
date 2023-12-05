package com.openai.voicenote.ui.screens.homeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.openai.voicenote.R
import com.openai.voicenote.ui.theme.VoiceNoteTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(homeViewModel: HomeViewModel = viewModel()) {

    val homeUiState by homeViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
           Box(
               modifier = Modifier.padding(16.dp)
           ) {
               TopAppBar(
                   modifier = Modifier
                       .clip(MaterialTheme.shapes.extraLarge)
                       .shadow(elevation = 24.dp)
                       .zIndex(10f)
                       .height(48.dp),
                   colors = TopAppBarDefaults.mediumTopAppBarColors(
                       containerColor = MaterialTheme.colorScheme.primaryContainer,
                       titleContentColor = MaterialTheme.colorScheme.primary,
                   ),
                   title = {
                       Box(
                           modifier = Modifier.fillMaxHeight(),
                           contentAlignment = Alignment.Center
                       ) {
                           Text(
                               text = stringResource(id = R.string.search_your_notes),
                               color = MaterialTheme.colorScheme.onBackground,
                               style = MaterialTheme.typography.titleMedium
                           )
                       }
                   },
                   navigationIcon = {
                       IconButton(onClick = { /* open navigation drawer */ }) {
                           Image(
                               painter = painterResource(
                                   id = R.drawable.menu
                               ),
                               contentDescription = "menu icon",
                               modifier = Modifier.size(20.dp)
                           )
                       }
                   },
                   actions = {
                       IconButton(
                           onClick = {
                               homeViewModel.toggleGridView()
                           }
                       ) {
                           Image(
                               painter = painterResource(
                                   id = checkGridStatus(homeUiState.isGridEnable)
                               ),
                               contentDescription = "view",
                               modifier = Modifier.size(28.dp)
                           )
                       }
                   },
               )
           }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* open voice recording screen*/ },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                elevation = FloatingActionButtonDefaults.elevation()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.mic),
                    contentDescription = "voice recording",
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {

        }
    }
}

fun checkGridStatus(gridEnable: Boolean) : Int {
    if(gridEnable) {
        return R.drawable.list
    }
    return R.drawable.grid
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    VoiceNoteTheme {
        Home()
    }
}

