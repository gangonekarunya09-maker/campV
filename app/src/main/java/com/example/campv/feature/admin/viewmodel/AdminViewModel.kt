package com.example.campv.feature.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campv.core.common.Result
import com.example.campv.core.common.UiState
import com.example.campv.data.model.Demand
import com.example.campv.data.repository.DemandRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AdminDemandStats(
    val totalCount: Int = 0,
    val pendingCount: Int = 0,
    val inProgressCount: Int = 0,
    val resolvedCount: Int = 0,
    val rejectedCount: Int = 0,
    val resolutionRatePercentage: Int = 0
)

class AdminViewModel(
    private val demandRepository: DemandRepository = DemandRepository()
) : ViewModel() {

    private val _rawDemands = MutableStateFlow<List<Demand>>(emptyList())

    private val _demandsState = MutableStateFlow<UiState<List<Demand>>>(UiState.Loading)
    val demandsState: StateFlow<UiState<List<Demand>>> = _demandsState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedStatus = MutableStateFlow("ALL")
    val selectedStatus: StateFlow<String> = _selectedStatus.asStateFlow()

    private val _statsState = MutableStateFlow(AdminDemandStats())
    val statsState: StateFlow<AdminDemandStats> = _statsState.asStateFlow()

    fun loadDemands(collegeId: String) {
        viewModelScope.launch {
            _demandsState.value = UiState.Loading
            when (val result = demandRepository.getDemandsByCollege(collegeId)) {
                is Result.Success -> {
                    val demands = result.data
                    _rawDemands.value = demands
                    calculateStats(demands)
                    applyFilterAndSearch()
                }
                is Result.Error -> {
                    _demandsState.value = UiState.Error(result.message, result.exception)
                }
                is Result.Loading -> {}
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        applyFilterAndSearch()
    }

    fun onStatusFilterSelected(status: String) {
        _selectedStatus.value = status
        applyFilterAndSearch()
    }

    private fun applyFilterAndSearch() {
        val query = _searchQuery.value.trim().lowercase()
        val statusFilter = _selectedStatus.value.uppercase()

        val filtered = _rawDemands.value.filter { demand ->
            val matchesStatus = (statusFilter == "ALL" || demand.status.uppercase() == statusFilter)
            val matchesQuery = query.isEmpty() ||
                    demand.title.lowercase().contains(query) ||
                    demand.description.lowercase().contains(query) ||
                    demand.studentName.lowercase().contains(query) ||
                    demand.category.lowercase().contains(query)
            matchesStatus && matchesQuery
        }

        if (filtered.isEmpty()) {
            _demandsState.value = UiState.Empty
        } else {
            _demandsState.value = UiState.Success(filtered)
        }
    }

    private fun calculateStats(demands: List<Demand>) {
        val total = demands.size
        val pending = demands.count { it.status.uppercase() == "PENDING" }
        val inProgress = demands.count { it.status.uppercase() == "IN_PROGRESS" }
        val resolved = demands.count { it.status.uppercase() == "RESOLVED" }
        val rejected = demands.count { it.status.uppercase() == "REJECTED" }
        val resolutionRate = if (total > 0) ((resolved.toDouble() / total) * 100).toInt() else 0

        _statsState.value = AdminDemandStats(
            totalCount = total,
            pendingCount = pending,
            inProgressCount = inProgress,
            resolvedCount = resolved,
            rejectedCount = rejected,
            resolutionRatePercentage = resolutionRate
        )
    }

    fun updateStatus(demandId: String, status: String, response: String, collegeId: String) {
        viewModelScope.launch {
            demandRepository.updateDemandStatus(demandId, status, response)
            loadDemands(collegeId)
        }
    }
}

