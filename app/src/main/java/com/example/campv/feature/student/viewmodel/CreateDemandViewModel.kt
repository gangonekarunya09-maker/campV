package com.example.campv.feature.student.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campv.core.common.Result
import com.example.campv.data.model.Demand
import com.example.campv.data.repository.DemandRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class CreateDemandViewModel(
    private val demandRepository: DemandRepository = DemandRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<CreateDemandUiState>(CreateDemandUiState.Idle)
    val uiState: StateFlow<CreateDemandUiState> =
        _uiState.asStateFlow()

    fun createDemand(
        title: String,
        description: String,
        category: String,
        collegeId: String,
        departmentId: String,
        studentId: String,
        studentName: String
    ) {
        if (title.isBlank()) {
            _uiState.value =
                CreateDemandUiState.Error("Title cannot be empty.")
            return
        }

        if (category.isBlank()) {
            _uiState.value =
                CreateDemandUiState.Error("Category cannot be empty.")
            return
        }

        if (description.isBlank()) {
            _uiState.value =
                CreateDemandUiState.Error("Description cannot be empty.")
            return
        }

        viewModelScope.launch {

            _uiState.value = CreateDemandUiState.Loading

            val demand = Demand(
                id = UUID.randomUUID().toString(),
                title = title.trim(),
                description = description.trim(),
                category = category.trim(),
                collegeId = collegeId,
                departmentId = departmentId,
                studentId = studentId,
                studentName = studentName,
                status = "PENDING"
            )

            when (val result = demandRepository.createDemand(demand)) {

                is Result.Success -> {
                    _uiState.value = CreateDemandUiState.Success
                }

                is Result.Error -> {
                    _uiState.value =
                        CreateDemandUiState.Error(result.message)
                }

                is Result.Loading -> Unit
            }
        }
    }

    fun resetState() {
        _uiState.value = CreateDemandUiState.Idle
    }
}
