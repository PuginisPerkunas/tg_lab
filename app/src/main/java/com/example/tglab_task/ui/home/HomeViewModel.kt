package com.example.tglab_task.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tglab_task.domain.GetTeamsUseCase
import com.example.tglab_task.models.TeamsResponse
import com.example.tglab_task.ui.Filter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTeamsUseCase: GetTeamsUseCase
): ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    init {
        viewModelScope.launch {
            makeRequest()
        }
    }

    fun tryAgainClick() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            makeRequest()
        }
    }

    fun newFilterSelected(selectedFilter: Filter) {
        _state.update { currentState ->
            currentState.copy(
                selectedFilter = selectedFilter,
                teams = currentState.teams.sortedBy {
                    when(selectedFilter){
                        Filter.NAME -> it.teamName
                        Filter.CITY -> it.teamCity
                        Filter.CONFERENCE -> it.teamConference
                    }
                }
            )
        }
    }

    private suspend fun makeRequest() {
        val request = getTeamsUseCase.execute(Unit)
        val result = request.getOrNull()
        if(result != null){
            _state.update { currentState ->
                currentState.copy(
                    isLoading = false,
                    teams = result.toTeams(currentState.selectedFilter)
                )
            }
        } else {
            _state.update { currentState ->
                currentState.copy(
                    isLoading = false,
                    teams = emptyList(),
                    errorMessage = (request as? com.example.tglab_task.utils.Result.Failure)?.error
                )
            }
        }
    }

    private fun TeamsResponse.toTeams(selectedFilter: Filter) = teams.map {
        TeamHomeComposeData(
            teamId = it.id.toString(),
            teamName = it.name,
            teamCity = it.city,
            teamConference = it.conference
        )
    }.sortedBy {
        when(selectedFilter){
            Filter.NAME -> it.teamName
            Filter.CITY -> it.teamCity
            Filter.CONFERENCE -> it.teamConference
        }
    }
}