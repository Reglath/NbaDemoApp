package com.example.nbademo

import androidx.lifecycle.SavedStateHandle
import com.example.nbademo.data.domain.model.Player
import com.example.nbademo.data.domain.model.Team
import com.example.nbademo.data.domain.repository.NbaRepository
import com.example.nbademo.ui.playerdetail.PlayerDetailViewModel
import com.example.nbademo.ui.playerlist.PlayerListViewModel
import com.example.nbademo.ui.teamdetail.TeamDetailViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class PlayerListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = mockk<NbaRepository>()

    @Test
    fun onInitializeLoadFirstPageAndUpdateState() = runTest {
        val mockPlayer = Player(
            id = 1, name = "LeBron James", position = "F", height = "6-9", weight = "250",
            jerseyNumber = "23", college = "N/A", country = "USA", draftInfo = "N/A",
            teamName = "Lakers", teamId = 14, imageUrl = ""
        )
        coEvery { repository.getPlayers(null) } returns Result.success(listOf(mockPlayer) to 2)

        val viewModel = PlayerListViewModel(repository)

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(1, state.players.size)
        assertEquals("LeBron James", state.players.first().name)
        assertEquals(null, state.error)
        assertFalse(state.endReached)
    }

    @Test
    fun onRepositoryErrorShowErrorMessage() = runTest {
        coEvery { repository.getPlayers(null) } returns Result.failure(Exception("Chyba sítě"))

        val viewModel = PlayerListViewModel(repository)

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(0, state.players.size)
        assertEquals("Chyba sítě", state.error)
    }
}

class PlayerDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = mockk<NbaRepository>()

    @Test
    fun ifPlayerIdValidInBundleLoadPlayer() = runTest {
        val playerId = 237
        val savedStateHandle = SavedStateHandle(mapOf("playerId" to playerId))

        val mockPlayer = Player(
            id = playerId, name = "LeBron James", position = "F", height = "6-9", weight = "250",
            jerseyNumber = "23", college = "N/A", country = "USA", draftInfo = "N/A",
            teamName = "Lakers", teamId = 14, imageUrl = ""
        )
        coEvery { repository.getPlayerById(playerId) } returns Result.success(mockPlayer)

        val viewModel = PlayerDetailViewModel(repository, savedStateHandle)

        val state = viewModel.state.value
        assertEquals(false, state.isLoading)
        assertEquals("LeBron James", state.player?.name)
        assertEquals(null, state.error)
    }

    @Test
    fun ifInvalidPlayerIdShowError() = runTest {
        val savedStateHandle = SavedStateHandle()

        val viewModel = PlayerDetailViewModel(repository, savedStateHandle)

        val state = viewModel.state.value
        assertEquals(false, state.isLoading)
        assertEquals(null, state.player)
        assertEquals("Chybí ID hráče", state.error)
    }
}

class TeamDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = mockk<NbaRepository>()

    @Test
    fun ifTeamIdValidInBundleLoadTeam() = runTest {
        val teamId = 14
        val savedStateHandle = SavedStateHandle(mapOf("teamId" to teamId))

        val mockTeam = Team(
            id = teamId, fullName = "Los Angeles Lakers", name = "Lakers",
            abbreviation = "LAL", city = "Los Angeles", conference = "West",
            division = "Pacific", imageUrl = "https://picsum.photos/seed/14/400"
        )
        coEvery { repository.getTeamById(teamId) } returns Result.success(mockTeam)

        val viewModel = TeamDetailViewModel(repository, savedStateHandle)

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals("Los Angeles Lakers", state.team?.fullName)
        assertNull(state.error)
    }

    @Test
    fun ifInvalidTeamIdShowError() = runTest {
        val savedStateHandle = SavedStateHandle()

        val viewModel = TeamDetailViewModel(repository, savedStateHandle)

        val state = viewModel.state.value
        assertEquals(false, state.isLoading)
        assertEquals(null, state.team)
        assertEquals("Chybí ID teamu", state.error)
    }
}