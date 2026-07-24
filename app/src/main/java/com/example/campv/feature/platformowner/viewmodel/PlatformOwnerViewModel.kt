package com.example.campv.feature.platformowner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campv.core.common.Result
import com.example.campv.core.common.UiState
import com.example.campv.data.model.College
import com.example.campv.data.repository.CollegeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlatformOwnerViewModel(
    private val collegeRepository: CollegeRepository = CollegeRepository()
) : ViewModel() {

    private val _collegesState = MutableStateFlow<UiState<List<College>>>(UiState.Loading)
    val collegesState: StateFlow<UiState<List<College>>> = _collegesState.asStateFlow()

    fun loadAllColleges() {
        viewModelScope.launch {
            _collegesState.value = UiState.Loading
            when (val result = collegeRepository.getAllColleges()) {
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        _collegesState.value = UiState.Empty
                    } else {
                        _collegesState.value = UiState.Success(result.data)
                    }
                }
                is Result.Error -> {
                    _collegesState.value = UiState.Error(result.message, result.exception)
                }
                is Result.Loading -> {}
            }
        }
    }

    fun setCollegeApproval(collegeId: String, isApproved: Boolean) {
        viewModelScope.launch {
            collegeRepository.updateCollegeStatus(collegeId, isApproved)
            loadAllColleges()
        }
    }
}
