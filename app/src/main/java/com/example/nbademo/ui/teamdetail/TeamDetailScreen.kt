package com.example.nbademo.ui.teamdetail

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
import com.example.nbademo.data.domain.model.Team
import com.example.nbademo.ui.theme.NbaDemoAppTheme
import com.example.nbademo.ui.util.AutoRetryEffect
import com.example.nbademo.ui.util.DetailRow

/**
 * Composable funkce pro zobrazení podrobných informací o konkrétním teamu.
 * Podporuje adaptivní rozvržení pro portrait i landscape.
 */
@Composable
fun TeamDetailScreen(viewModel: TeamDetailViewModel) {
    val state by viewModel.state.collectAsState()

    AutoRetryEffect(state.error) { viewModel.retryLoad() }

    TeamDetailContent(state = state)
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TeamDetailContent(state: TeamDetailUiState) {
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

    state.team?.let { team ->
        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp, horizontal = 60.dp)
            ) {
                GlideImage(
                    model = team.imageUrl,
                    contentDescription = "Logo teamu ${team.name}",
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
                    TeamDetailCard(team)
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
                    model = team.imageUrl,
                    contentDescription = "Fotka hráče ${team.name}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                TeamDetailCard(team)
            }
        }
    }
}

@Composable
fun TeamDetailCard(team: Team) {
    Text(
        text = team.fullName,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.testTag("team_name")
    )

    Spacer(modifier = Modifier.height(8.dp))

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(top = 8.dp)) {
            DetailRow("Název", team.name)
            DetailRow("Zkratka", team.abbreviation)
            DetailRow("Město", team.city)
            DetailRow("Konference", team.conference)
            DetailRow("Divize", team.division)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TeamDetailPreview() {
    NbaDemoAppTheme {
        TeamDetailContent(
            state = TeamDetailUiState(
                team = Team(
                    id = 14,
                    fullName = "Los Angeles Lakers",
                    name = "Lakers",
                    abbreviation = "LAL",
                    city = "Los Angeles",
                    conference = "West",
                    division = "Pacific",
                    imageUrl = "https://ui-avatars.com/api/?name=Lakers&background=random&size=400"
                )
            )
        )
    }
}