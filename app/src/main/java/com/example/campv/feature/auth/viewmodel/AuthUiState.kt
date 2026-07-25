package com.example.campv.feature.auth.viewmodel

import com.example.campv.data.model.User

sealed interface AuthUiState {

    data object Idle : AuthUiState

    data object Loading : AuthUiState

    data class Success(
        val user: User
    ) : AuthUiState

    data class Error(
        val message: String,
        val throwable: Throwable? = null
    ) : AuthUiState
}