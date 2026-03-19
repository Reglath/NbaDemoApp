package com.example.nbademo.ui.playerdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nbademo.data.domain.repository.NbaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel pro obrazovku detailu hráče.
 * Zajišťuje načtení dat na základě ID z [SavedStateHandle].
 */
@HiltViewModel
class PlayerDetailViewModel @Inject constructor(
    private val repository: NbaRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(PlayerDetailUiState())
    val state = _state.asStateFlow()

    private val playerId: Int = savedStateHandle.get<Int>("playerId") ?: -1

    init {
        if (playerId != -1) {
            loadPlayerDetail(playerId)
        } else {
            _state.update { it.copy(isLoading = false, error = "Chybí ID hráče") }
        }
    }

    fun retryLoad() {
        if (playerId != -1) {
            loadPlayerDetail(playerId)
        }
    }

    private fun loadPlayerDetail(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            repository.getPlayerById(id).onSuccess { player ->
                _state.update { it.copy(isLoading = false, player = player, error = null) }
            }.onFailure { e ->
                _state.update { it.copy(isLoading = false, error = e.localizedMessage) }
            }
        }
    }
}