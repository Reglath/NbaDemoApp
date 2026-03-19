package com.example.nbademo

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.example.nbademo.data.domain.model.Player
import com.example.nbademo.ui.playerdetail.PlayerDetailContent
import com.example.nbademo.ui.playerdetail.PlayerDetailUiState
import org.junit.Rule
import org.junit.Test

class PlayerDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun detailDisplaysLoaderWhileLoading() {
        val state = PlayerDetailUiState(isLoading = true)

        composeTestRule.setContent {
            PlayerDetailContent(
                state = state,
                onNavigateToTeam = {},
            )
        }

        composeTestRule.onNodeWithTag("loader").assertIsDisplayed()
    }

    @Test
    fun detailDisplaysErrorOnError() {
        val errorMessage = "Hráč nenalezen"
        val state = PlayerDetailUiState(isLoading = false, error = errorMessage)

        composeTestRule.setContent {
            PlayerDetailContent(
                state = state,
                onNavigateToTeam = {},
            )
        }

        composeTestRule.onNodeWithTag("error_text")
            .assertIsDisplayed()
            .assertTextEquals(errorMessage)
    }

    @Test
    fun playerDetailShowsAfterLoading() {
        val mockPlayer = createMockPlayer()
        val state = PlayerDetailUiState(isLoading = false, player = mockPlayer)

        composeTestRule.setContent {
            PlayerDetailContent(state = state, onNavigateToTeam = {})
        }

        composeTestRule.onNodeWithTag("loader").assertDoesNotExist()
        composeTestRule.onNodeWithTag("player_name")
            .assertIsDisplayed()
            .assertTextEquals(mockPlayer.name)
    }

    private fun createMockPlayer() = Player(
        id = 237,
        name = "LeBron James",
        position = "F",
        height = "6-9",
        weight = "250",
        jerseyNumber = "23",
        college = "N/A",
        country = "USA",
        draftInfo = "2003 R1 Pick 1",
        teamName = "Los Angeles Lakers",
        teamId = 14,
        imageUrl = ""
    )
}
