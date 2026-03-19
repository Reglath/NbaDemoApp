package com.example.nbademo

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.nbademo.data.domain.model.Player
import com.example.nbademo.ui.playerlist.PlayerListItem
import org.junit.Rule
import org.junit.Test

class PlayerListItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun playerListItemShowsCorrectDataAndReactsOnClick() {
        var clickedPlayerId = -1
        val mockPlayer = createMockPlayer()

        composeTestRule.setContent {
            PlayerListItem(
                player = mockPlayer,
                onPlayerClick = { clickedPlayerId = it }
            )
        }

        composeTestRule.onNodeWithText("LeBron James").assertIsDisplayed()
        composeTestRule.onNodeWithText("Pozice: F").assertIsDisplayed()
        composeTestRule.onNodeWithText("LeBron James").performClick()

        assert(clickedPlayerId == 237)
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