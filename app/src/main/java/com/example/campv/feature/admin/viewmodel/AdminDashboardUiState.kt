package com.example.campv.feature.admin.viewmodel

import com.example.campv.data.model.Demand

sealed interface AdminDashboardUiState {

    data object Loading : AdminDashboardUiState

    data class Success(
        val demands: List<Demand>
    ) : AdminDashboardUiState

    data object Empty : AdminDashboardUiState

    data class Error(
        val message: String
    ) : AdminDashboardUiState
}