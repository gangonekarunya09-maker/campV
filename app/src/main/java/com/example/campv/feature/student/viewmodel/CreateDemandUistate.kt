package com.example.campv.feature.student.viewmodel

sealed interface CreateDemandUiState {

    data object Idle : CreateDemandUiState

    data object Loading : CreateDemandUiState

    data object Success : CreateDemandUiState

    data class Error(
        val message: String
    ) : CreateDemandUiState
}