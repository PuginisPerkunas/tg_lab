package com.example.tglab_task.ui.players

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.tglab_task.models.PlayerData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedPlayersViewModel @Inject constructor(): ViewModel() {

    var selectedPlayerData by mutableStateOf<PlayerData?>(null)
        private set

    fun setPlayer(playerData: PlayerData){
        selectedPlayerData = playerData
    }
}