package com.example.campv.feature.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campv.core.common.Result
import com.example.campv.data.model.Demand
import com.example.campv.data.repository.DemandRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminDashboardViewModel(
    private val demandRepository: DemandRepository = DemandRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<AdminDashboardUiState>(AdminDashboardUiState.Loading)
    val uiState: StateFlow<AdminDashboardUiState> = _uiState.asStateFlow()

    private var allDemands: List<Demand> = emptyList()

    fun loadDemands(collegeId: String) {
        viewModelScope.launch {

            _uiState.value = AdminDashboardUiState.Loading

            when (val result = demandRepository.getDemandsByCollege(collegeId)) {

                is Result.Success -> {

                    allDemands = result.data

                    _uiState.value =
                        if (allDemands.isEmpty()) {
                            AdminDashboardUiState.Empty
                        } else {
                            AdminDashboardUiState.Success(allDemands)
                        }
                }

                is Result.Error -> {
                    _uiState.value =
                        AdminDashboardUiState.Error(result.message)
                }

                is Result.Loading -> Unit
            }
        }
    }
    fun search(query: String) {

        val filtered = if (query.isBlank()) {

            allDemands

        } else {

            allDemands.filter {

                it.title.contains(query, true) ||
                        it.description.contains(query, true)

            }
        }

        _uiState.value =
            if (filtered.isEmpty()) {
                AdminDashboardUiState.Empty
            } else {
                AdminDashboardUiState.Success(filtered)
            }
    }
    fun filterByStatus(status: String) {

        if (status == "ALL") {

            _uiState.value = AdminDashboardUiState.Success(allDemands)
            return
        }

        val filtered = allDemands.filter {

            it.status.equals(status, ignoreCase = true)

        }

        _uiState.value =
            if (filtered.isEmpty()) {
                AdminDashboardUiState.Empty
            } else {
                AdminDashboardUiState.Success(filtered)
            }
    }

    fun getDemandStatistics(): DemandStatistics {

        return DemandStatistics(
            total = allDemands.size,
            pending = allDemands.count { it.status == "PENDING" },
            approved = allDemands.count { it.status == "APPROVED" },
            rejected = allDemands.count { it.status == "REJECTED" },
            resolved = allDemands.count { it.status == "RESOLVED" }
        )
    }
}