package com.example.nbademo.ui.teamdetail

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
 * ViewModel pro obrazovku detailu teamu.
 * Zajišťuje načtení dat na základě ID z [SavedStateHandle].
 */
@HiltViewModel
class TeamDetailViewModel @Inject constructor(
    private val repository: NbaRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(TeamDetailUiState())
    val state = _state.asStateFlow()

    private val teamId: Int = savedStateHandle.get<Int>("teamId") ?: -1

    init {
        if (teamId != -1) {
            loadTeamDetail(teamId)
        } else {
            _state.update { it.copy(isLoading = false, error = "Chybí ID teamu") }
        }
    }

    fun retryLoad() {
        if (teamId != -1 && !_state.value.isLoading) {
            loadTeamDetail(teamId)
        }
    }

    private fun loadTeamDetail(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            repository.getTeamById(id).onSuccess { team ->
                _state.update { it.copy(isLoading = false, team = team, error = null) }
            }.onFailure { e ->
                _state.update { it.copy(isLoading = false, error = e.localizedMessage) }
            }
        }
    }
}