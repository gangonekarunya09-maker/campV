package com.example.campv.feature.student.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campv.core.common.Result
import com.example.campv.core.common.UiState
import com.example.campv.data.model.Demand
import com.example.campv.data.repository.DemandRepository
import com.example.campv.data.repository.VoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StudentDashboardViewModel(
    private val demandRepository: DemandRepository = DemandRepository(),
    private val voteRepository: VoteRepository = VoteRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Demand>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Demand>>> = _uiState.asStateFlow()

    fun loadDemands(collegeId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            when (val result = demandRepository.getDemandsByCollege(collegeId)) {
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        _uiState.value = UiState.Empty
                    } else {
                        _uiState.value = UiState.Success(result.data)
                    }
                }
                is Result.Error -> {
                    _uiState.value = UiState.Error(result.message, result.exception)
                }
                is Result.Loading -> {}
            }
        }
    }

    fun upvoteDemand(demandId: String, userId: String, collegeId: String) {
        viewModelScope.launch {
            voteRepository.upvoteDemand(demandId, userId)
            loadDemands(collegeId)
        }
    }
}
