package com.example.nbademo.ui.playerlist

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
 */
@HiltViewModel
class PlayerListViewModel @Inject constructor(
    private val repository: NbaRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PlayerListUiState())
    val state = _state.asStateFlow()

    private var currentCursor: Int? = null

    init {
        loadNextPlayers()
    }

    fun loadNextPlayers() {
        if (_state.value.isLoading || _state.value.endReached) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val result = repository.getPlayers(currentCursor)

            result.onSuccess { (newPlayers, nextCursor) ->
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        players = currentState.players + newPlayers,
                        endReached = nextCursor == null,
                        error = null
                    )
                }
                currentCursor = nextCursor
            }.onFailure { exception ->
                _state.update {
                    it.copy(isLoading = false, error = exception.localizedMessage)
                }
            }
        }
    }
}