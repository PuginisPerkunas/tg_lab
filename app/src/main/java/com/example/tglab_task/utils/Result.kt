package com.example.tglab_task.utils

sealed class Result<out T> {
    data class Success<out T>(val value: T) : Result<T>()
    data class Failure<out T>(val error: String?) : Result<T>()

    fun getOrNull(): T? = when (this) {
        is Success -> value
        is Failure -> null
    }
}

