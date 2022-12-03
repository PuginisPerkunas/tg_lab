package com.example.tglab_task.domain

import com.example.tglab_task.data.TgLabRepository
import com.example.tglab_task.models.TeamGamesResponse
import com.example.tglab_task.utils.Result
import com.example.tglab_task.utils.UseCase
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
data class GetGamesUseCase @Inject constructor(
    private val repository: TgLabRepository,
) : UseCase<GetGamesUseCase.Params, TeamGamesResponse> {

    data class Params(
        val teamId: String,
        val page: Int,
    )

    override suspend fun execute(params: Params?): Result<TeamGamesResponse> {
        requireNotNull(params)
        // Todo should be custom wrap on request self using retrofit
        //  .addCallAdapterFactory(...Factory())
        return try {
            val result = repository.getTeamGames(
                teamId = params.teamId,
                page = params.page
            )
            return Result.Success(value = result)
        } catch (exception: Exception) {
            Result.Failure(error = exception.message)
        }
    }
}