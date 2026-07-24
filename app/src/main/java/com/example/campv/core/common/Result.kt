package com.example.campv.core.common

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable, val message: String = exception.localizedMessage ?: "Unknown Error") : Result<Nothing>()
    data object Loading : Result<Nothing>()
}
