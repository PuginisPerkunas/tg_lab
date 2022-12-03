package com.example.tglab_task.ui.players

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.tglab_task.data.TgLabRepository
import com.example.tglab_task.models.PlayerData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayersViewModel @Inject constructor(
    private val repository: TgLabRepository
) : ViewModel() {

    private val _textSearch = mutableStateOf("")
    val textSearch: State<String> = _textSearch

    private val _searchedPlayers = MutableStateFlow<PagingData<PlayerData>>(PagingData.empty())
    val searchedPlayers = _searchedPlayers

    init {
        viewModelScope.launch { loadPlayers() }
    }

    fun setPlayerQuery(query: String) {
        _textSearch.value = query
        viewModelScope.launch { loadPlayers() }
    }

    fun clearText() {
        _textSearch.value = ""
        viewModelScope.launch { loadPlayers() }
    }

    fun tryAgainClick() {
        viewModelScope.launch { loadPlayers() }
    }

    private suspend fun loadPlayers() {
        repository.getPlayersResultStream(query = _textSearch.value).cachedIn(viewModelScope)
            .collect {
                _searchedPlayers.value = it
            }
    }
}
