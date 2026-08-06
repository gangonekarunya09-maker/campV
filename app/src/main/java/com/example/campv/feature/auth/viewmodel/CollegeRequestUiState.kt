package com.example.campv.feature.auth.viewmodel

sealed interface CollegeRequestUiState {

    /**
     * Initial screen state.
     */
    data object Idle : CollegeRequestUiState

    /**
     * Request is being submitted.
     */
    data object Loading : CollegeRequestUiState

    /**
     * Request submitted successfully.
     */
    data class Success(
        val message: String = "College registration request submitted successfully."
    ) : CollegeRequestUiState

    /**
     * Submission failed.
     */
    data class Error(
        val message: String
    ) : CollegeRequestUiState
}