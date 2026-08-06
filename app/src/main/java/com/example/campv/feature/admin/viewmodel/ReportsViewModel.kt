package com.example.campv.feature.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campv.core.common.Result
import com.example.campv.core.common.UiState
import com.example.campv.data.model.DemandStatistics
import com.example.campv.data.repository.DemandRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReportsViewModel(
    private val demandRepository: DemandRepository = DemandRepository()
) : ViewModel() {

    private val _statsState = MutableStateFlow<UiState<DemandStatistics>>(UiState.Loading)
    val statsState: StateFlow<UiState<DemandStatistics>> = _statsState.asStateFlow()

    fun loadReports(collegeId: String) {
        viewModelScope.launch {
            _statsState.value = UiState.Loading
            when (val result = demandRepository.getDemandsByCollege(collegeId)) {
                is Result.Success -> {
                    val demands = result.data
                    val total = demands.size
                    val pending = demands.count { it.status.uppercase() == "PENDING" }
                    val inProgress = demands.count { it.status.uppercase() == "IN_PROGRESS" }
                    val resolved = demands.count { it.status.uppercase() == "RESOLVED" }
                    val rejected = demands.count { it.status.uppercase() == "REJECTED" }
                    val rate = if (total > 0) ((resolved.toDouble() / total) * 100).toInt() else 0
                    val avgUpvotes = if (total > 0) demands.map { it.upvotesCount }.average() else 0.0

                    val stats = DemandStatistics(
                        totalDemands = total,
                        pendingDemands = pending,
                        inProgressDemands = inProgress,
                        resolvedDemands = resolved,
                        rejectedDemands = rejected,
                        resolutionRatePercentage = rate,
                        averageUpvotes = avgUpvotes
                    )
                    _statsState.value = UiState.Success(stats)
                }
                is Result.Error -> {
                    _statsState.value = UiState.Error(result.message, result.exception)
                }
                is Result.Loading -> {}
            }
        }
    }
}
