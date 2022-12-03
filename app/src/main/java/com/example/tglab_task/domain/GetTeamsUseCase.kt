package com.example.tglab_task.domain

import com.example.tglab_task.data.TgLabRepository
import com.example.tglab_task.models.TeamsResponse
import com.example.tglab_task.utils.Result
import com.example.tglab_task.utils.UseCase
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetTeamsUseCase @Inject constructor(
    private val repository: TgLabRepository
): UseCase<Unit, TeamsResponse> {
    override suspend fun execute(params: Unit?): Result<TeamsResponse> {
        requireNotNull(params)
        return try {
            val result = repository.getTeams()
            return Result.Success(value = result)
        } catch (exception: Exception){
            Result.Failure( error = exception.message)
        }
    }
}