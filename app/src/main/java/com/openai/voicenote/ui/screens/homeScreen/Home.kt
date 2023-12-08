package com.openai.voicenote.ui.screens.homeScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.openai.voicenote.R
import com.openai.voicenote.model.Note
import com.openai.voicenote.ui.navigation.NavigationItem
import com.openai.voicenote.ui.theme.VoiceNoteTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    navHostController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

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
                onClick = {
                    navHostController.navigate(NavigationItem.VoiceRecord.route)
                },
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
    ) {padding ->
        LazyVerticalStaggeredGrid(
            modifier = Modifier.padding(padding).padding(start = 8.dp, end = 8.dp, bottom = 16.dp),
            columns = StaggeredGridCells.Fixed(countColumn(homeUiState.isGridEnable)),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            flingBehavior = ScrollableDefaults.flingBehavior()
        ) {
            header {
                Text(
                    text = stringResource(id = R.string.pinned),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .paddingFromBaseline(bottom = 8.dp)
                        .padding(top = 16.dp, start = 16.dp),
                    fontWeight = FontWeight.Bold
                )
            }
            items(homeUiState.allPinNotes) {
                RenderGridItem(note = it)
            }
            header {
                Text(
                    text = stringResource(id = R.string.others),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .paddingFromBaseline(bottom = 8.dp)
                        .padding(top = 16.dp, start = 16.dp),
                    fontWeight = FontWeight.Bold
                )
            }
            items(homeUiState.allOtherNotes) {
                RenderGridItem(note = it)
            }
        }
    }
}

fun LazyStaggeredGridScope.header(
    content: @Composable LazyStaggeredGridItemScope.() -> Unit
) {
    item(
        span = StaggeredGridItemSpan.FullLine,
        content = content
    )
}

@Composable
fun RenderGridItem(note : Note) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = MaterialTheme.shapes.large
    ) {
        Text(
            text = note.title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.W900,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 4.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = note.description,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 16.dp),
            maxLines = 5,
            overflow = TextOverflow.Ellipsis
        )
    }
}

fun countColumn(gridEnable: Boolean) : Int {
    if (gridEnable) {
        return 2
    }
    return 1
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
        Home(navHostController = rememberNavController())
    }
}

