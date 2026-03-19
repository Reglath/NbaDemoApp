package com.example.nbademo.ui.playerdetail

import com.example.nbademo.data.domain.model.Player

/**
 * Reprezentuje stav uživatelského rozhraní pro obrazovku detailu hráče.
 */
data class PlayerDetailUiState(
    val isLoading: Boolean = false,
    val player: Player? = null,
    val error: String? = null
)