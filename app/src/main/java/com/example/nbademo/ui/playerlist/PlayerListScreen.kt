package com.example.nbademo.ui.playerlist

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nbademo.data.domain.model.Player
import com.example.nbademo.ui.theme.NbaDemoAppTheme
import kotlinx.coroutines.delay

/**
 * Hlavní obrazovka aplikace zobrazující stránkovaný seznam hráčů NBA.
 * Obsahuje logiku pro nekonečné scrollování, automatický refresh při error 429,
 * a navigaci na detail hráče.
 */
@Composable
fun PlayerListScreen(
    viewModel: PlayerListViewModel,
    onNavigateToDetail: (Int) -> Unit
) {
    val state by viewModel.state.collectAsState()

    PlayerListContent(
        state = state,
        onNavigateToDetail = onNavigateToDetail,
        onLoadMore = { viewModel.loadNextPlayers() }
    )
}

@Composable
fun PlayerListContent(
    state: PlayerListUiState,
    onNavigateToDetail: (Int) -> Unit,
    onLoadMore: () -> Unit
) {
    val listState = rememberLazyListState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val shouldLoadMore by remember {
        derivedStateOf {
            val totalItemsCount = listState.layoutInfo.totalItemsCount
            val lastVisibleItemIndex =
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            lastVisibleItemIndex >= totalItemsCount - 5 && totalItemsCount > 0
        }
    }

    LaunchedEffect(shouldLoadMore, state.error, state.isLoading) {
        if (shouldLoadMore && !state.isLoading && !state.endReached) {

            if (state.error != null) {
                delay(5000)
            }

            onLoadMore()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (isLandscape) {
                        Modifier.padding(horizontal = 60.dp)
                    } else {
                        Modifier
                    }
                )
        ) {
            items(
                items = state.players,
                key = { player -> player.id }
            ) { player ->
                PlayerListItem(
                    player = player,
                    onPlayerClick = onNavigateToDetail
                )
            }

            item {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .wrapContentWidth()
                    )
                } else if (state.error != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayerListPreview() {
    NbaDemoAppTheme {
        PlayerListContent(
            state = PlayerListUiState(
                players = listOf(
                    Player(
                        id = 237,
                        name = "LeBron James",
                        position = "F",
                        height = "6-9",
                        weight = "250",
                        jerseyNumber = "23",
                        college = "No College",
                        country = "USA",
                        draftInfo = "2003 R1 Pick 1",
                        teamName = "Los Angeles Lakers",
                        teamId = 14,
                        imageUrl = "https://ui-avatars.com/api/?name=LeBron+James&background=random&size=400"
                    ),
                    Player(
                        id = 115,
                        name = "Stephen Curry",
                        position = "G",
                        height = "6-2",
                        weight = "185",
                        jerseyNumber = "30",
                        college = "Davidson",
                        country = "USA",
                        draftInfo = "2009 R1 Pick 7",
                        teamName = "Golden State Warriors",
                        teamId = 10,
                        imageUrl = "https://ui-avatars.com/api/?name=Stephen+Curry&background=random&size=400"
                    )
                )
            ),
            onNavigateToDetail = {},
            onLoadMore = {}
        )
    }
}