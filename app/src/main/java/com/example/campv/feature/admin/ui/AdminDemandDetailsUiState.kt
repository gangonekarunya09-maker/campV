package com.example.campv.feature.admin.viewmodel

import com.example.campv.data.model.Comment
import com.example.campv.data.model.Demand

sealed interface AdminDemandDetailsUiState {

    data object Loading : AdminDemandDetailsUiState

    data class Success(
        val demand: Demand,
        val comments: List<Comment>
    ) : AdminDemandDetailsUiState

    data object Empty : AdminDemandDetailsUiState

    data class Error(
        val message: String
    ) : AdminDemandDetailsUiState
}