package com.example.nbademo

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.example.nbademo.data.domain.model.Team
import com.example.nbademo.ui.teamdetail.TeamDetailContent
import com.example.nbademo.ui.teamdetail.TeamDetailUiState
import org.junit.Rule
import org.junit.Test

class TeamDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun detailDisplaysLoaderWhileLoading() {
        val state = TeamDetailUiState(isLoading = true)

        composeTestRule.setContent {
            TeamDetailContent(
                state = state,
            )
        }

        composeTestRule.onNodeWithTag("loader").assertIsDisplayed()
    }

    @Test
    fun detailDisplaysErrorOnError() {
        val errorMessage = "Hráč nenalezen"
        val state = TeamDetailUiState(isLoading = false, error = errorMessage)

        composeTestRule.setContent {
            TeamDetailContent(
                state = state,
            )
        }

        composeTestRule.onNodeWithTag("error_text")
            .assertIsDisplayed()
            .assertTextEquals(errorMessage)
    }

    @Test
    fun teamDetailShowsAfterLoading() {
        val mockTeam = createMockTeam()
        val state = TeamDetailUiState(isLoading = false, team = mockTeam)

        composeTestRule.setContent {
            TeamDetailContent(state = state)
        }

        composeTestRule.onNodeWithTag("loader").assertDoesNotExist()
        composeTestRule.onNodeWithTag("team_name")
            .assertIsDisplayed()
    }

    private fun createMockTeam() = Team(
        id = 14,
        fullName = "Los Angeles Lakers",
        name = "Lakers",
        abbreviation = "LAL",
        city = "Los Angeles",
        conference = "West",
        division = "Pacific",
        imageUrl = ""
    )
}