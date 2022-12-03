package com.example.tglab_task.utils

interface UseCase<Param, Output> {
    suspend fun execute(params: Param? = null): Result<Output>
}
