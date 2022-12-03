package com.example.tglab_task.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.tglab_task.di.TGLabApi
import com.example.tglab_task.domain.PlayerPagingSource
import com.example.tglab_task.models.PlayerData
import com.example.tglab_task.models.TeamGamesResponse
import com.example.tglab_task.models.TeamsResponse
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Should be multiple repos, but for app needs - go with 1
@ViewModelScoped
class TgLabRepository @Inject constructor(
    // Could have remote/local source implementations
    private val tgLabApi: TGLabApi,
) {
    suspend fun getTeamGames(teamId: String, page: Int): TeamGamesResponse {
        return tgLabApi.getTeamGames(
            teamId = teamId,
            page = page
        )
    }

    suspend fun getTeams(): TeamsResponse {
        return tgLabApi.getTeams()
    }

    fun getPlayersResultStream(
        query: String
    ): Flow<PagingData<PlayerData>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PlayerPagingSource(
                query = query,
                api = tgLabApi
            ) }
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }
}