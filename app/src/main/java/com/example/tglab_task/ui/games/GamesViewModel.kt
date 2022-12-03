package com.example.tglab_task.ui.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.tglab_task.domain.GetGamesUseCase
import com.example.tglab_task.models.PlayerData
import com.example.tglab_task.models.TeamGame
import com.example.tglab_task.models.TeamGamesResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
// Overall should be implemented using Pager 3 but want to show that custom solution is possible
class GamesViewModel @Inject constructor(
    private val getGamesUseCase: GetGamesUseCase
): ViewModel(){

    private val _state = MutableStateFlow(GamesScreenState())
    val state: StateFlow<GamesScreenState> = _state

    fun setTeamInfo(
        teamName: String,
        teamId: String
    ){
        _state.update { currentState ->
            currentState.copy(
                teamId = teamId,
                title = teamName
            )
        }
        loadPage()
    }

    fun loadNextPage() {
        loadPage()
    }

    // Bug behavior because of BE
    private fun loadPage() {
        viewModelScope.launch {
            val nextPage = _state.value.nextPage
            if (!_state.value.isLoading && nextPage != null) {
                _state.update { it.copy(isLoading = true) }
                val result = getGamesUseCase.execute(
                    params = GetGamesUseCase.Params(
                        teamId = _state.value.teamId,
                        page = nextPage
                    )
                )
                val resultData = result.getOrNull()
                if (resultData != null) {
                    val allGames = _state.value.games + resultData.toGames(_state.value.teamId)
                    _state.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            games = allGames,
                            currentPage = resultData.meta.current_page,
                            nextPage = resultData.meta.next_page
                        )
                    }
                    if (allGames.size < 25 && resultData.meta.next_page != null) {
                        loadPage()
                    }
                    // BE workaround (returns empty list but next page exist)
                    if (resultData.teamGames.isEmpty() && resultData.meta.next_page != null) {
                        loadPage()
                    }
                } else {
                    updateStateOnError((result as? com.example.tglab_task.utils.Result.Failure)?.error)
                }
            }
        }
    }

    private fun updateStateOnError(message: String?){
        _state.update { currentState ->
            currentState.copy(
                isLoading = false,
                error = if(currentState.games.isEmpty()) message else null,
            )
        }
    }

    private fun TeamGamesResponse.toGames(selectedTeamId: String) = teamGames.mapNotNull {
        if(it.home_team.id.toString() == selectedTeamId){
            GameComposeData(
                homeName = it.home_team.full_name,
                homeScore = it.home_team_score,
                visitorName = it.visitor_team.full_name,
                visitorScore = it.visitor_team_score
            )
        } else null
    }
}