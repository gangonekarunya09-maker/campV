package com.example.campv.core.common

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<out T>(val data: T) : UiState<T>
    data class Error(val message: String, val exception: Throwable? = null) : UiState<Nothing>
    data object Empty : UiState<Nothing>
}
