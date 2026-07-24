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

class AdminViewModel(
    private val demandRepository: DemandRepository = DemandRepository()
) : ViewModel() {

    private val _demandsState = MutableStateFlow<UiState<List<Demand>>>(UiState.Loading)
    val demandsState: StateFlow<UiState<List<Demand>>> = _demandsState.asStateFlow()

    fun loadDemands(collegeId: String) {
        viewModelScope.launch {
            _demandsState.value = UiState.Loading
            when (val result = demandRepository.getDemandsByCollege(collegeId)) {
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        _demandsState.value = UiState.Empty
                    } else {
                        _demandsState.value = UiState.Success(result.data)
                    }
                }
                is Result.Error -> {
                    _demandsState.value = UiState.Error(result.message, result.exception)
                }
                is Result.Loading -> {}
            }
        }
    }

    fun updateStatus(demandId: String, status: String, response: String, collegeId: String) {
        viewModelScope.launch {
            demandRepository.updateDemandStatus(demandId, status, response)
            loadDemands(collegeId)
        }
    }
}
