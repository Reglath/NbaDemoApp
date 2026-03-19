package com.example.nbademo.ui.playerdetail

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.nbademo.data.domain.model.Player
import com.example.nbademo.ui.theme.NbaDemoAppTheme
import com.example.nbademo.ui.util.AutoRetryEffect
import com.example.nbademo.ui.util.ClickableDetailRow
import com.example.nbademo.ui.util.DetailRow

/**
 * Composable funkce pro zobrazení podrobných informací o konkrétním hráči.
 * Podporuje adaptivní rozvržení pro portrait i landscape.
 */
@Composable
fun PlayerDetailScreen(viewModel: PlayerDetailViewModel, onNavigateToTeam: (Int) -> Unit) {
    val state by viewModel.state.collectAsState()

    AutoRetryEffect(state.error) { viewModel.retryLoad() }

    PlayerDetailContent(state = state, onNavigateToTeam = onNavigateToTeam)
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PlayerDetailContent(
    state: PlayerDetailUiState,
    onNavigateToTeam: (Int) -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (state.isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .testTag("loader"),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    }

    state.error?.let { error ->
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.testTag("error_text")
            )
            Spacer(modifier = Modifier.height(8.dp))
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )
        }
    }

    state.player?.let { player ->
        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp, horizontal = 60.dp)
            ) {
                GlideImage(
                    model = player.imageUrl,
                    contentDescription = "Fotka hráče ${player.name}",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
                ) {
                    PlayerDetailCard(player = player, onNavigateToTeam = onNavigateToTeam)
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                GlideImage(
                    model = player.imageUrl,
                    contentDescription = "Fotka hráče ${player.name}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                PlayerDetailCard(player = player, onNavigateToTeam = onNavigateToTeam)
            }
        }
    }
}

@Composable
fun PlayerDetailCard(
    player: Player,
    onNavigateToTeam: (Int) -> Unit
) {
    Text(
        text = player.name,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.testTag("player_name")
    )

    Spacer(modifier = Modifier.height(8.dp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(top = 8.dp)) {
            DetailRow("Číslo dresu", player.jerseyNumber)
            DetailRow("Pozice", player.position)
            DetailRow("Výška", player.height)
            DetailRow("Váha", player.weight)
            DetailRow("Země", player.country)
            DetailRow("Univerzita", player.college)
            DetailRow("Draft", player.draftInfo)

            ClickableDetailRow(
                label = "Klub",
                value = player.teamName,
                onClick = { if (player.teamId != -1) onNavigateToTeam(player.teamId) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayerDetailPreview() {
    NbaDemoAppTheme {
        PlayerDetailContent(
            state = PlayerDetailUiState(
                player = Player(
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
                    imageUrl = "https://ui-avatars.com/api/?name=Lebron+James&background=random&size=400"
                ),

                ),
            onNavigateToTeam = {}
        )
    }
}