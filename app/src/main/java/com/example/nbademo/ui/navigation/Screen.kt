package com.example.nbademo.ui.navigation

/**
 * Definice navigačních tras a parametrů pro celou aplikaci.
 * Využívá sealed class pro type=safety.
 */
sealed class Screen(val route: String) {
    object PlayerList : Screen("player_list")
    object PlayerDetail : Screen("player_detail/{playerId}") {
        fun createRoute(playerId: Int) = "player_detail/$playerId"
    }
    object TeamDetail : Screen("team_detail/{teamId}") {
        fun createRoute(teamId: Int) = "team_detail/$teamId"
    }
}