package com.example.tglab_task.domain

import com.example.tglab_task.data.TgLabRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetPlayersUseCase @Inject constructor(
    private val repository: TgLabRepository
) {

}