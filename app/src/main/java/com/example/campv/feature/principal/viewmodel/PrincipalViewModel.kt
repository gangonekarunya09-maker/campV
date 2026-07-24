package com.example.campv.feature.principal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campv.core.common.Result
import com.example.campv.core.common.UiState
import com.example.campv.data.model.Department
import com.example.campv.data.model.User
import com.example.campv.data.repository.CollegeRepository
import com.example.campv.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class PrincipalViewModel(
    private val collegeRepository: CollegeRepository = CollegeRepository(),
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _usersState = MutableStateFlow<UiState<List<User>>>(UiState.Loading)
    val usersState: StateFlow<UiState<List<User>>> = _usersState.asStateFlow()

    private val _departmentsState = MutableStateFlow<UiState<List<Department>>>(UiState.Loading)
    val departmentsState: StateFlow<UiState<List<Department>>> = _departmentsState.asStateFlow()

    fun loadCollegeUsers(collegeId: String) {
        viewModelScope.launch {
            _usersState.value = UiState.Loading
            when (val result = userRepository.getUsersByCollege(collegeId)) {
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        _usersState.value = UiState.Empty
                    } else {
                        _usersState.value = UiState.Success(result.data)
                    }
                }
                is Result.Error -> {
                    _usersState.value = UiState.Error(result.message, result.exception)
                }
                is Result.Loading -> {}
            }
        }
    }

    fun loadDepartments(collegeId: String) {
        viewModelScope.launch {
            _departmentsState.value = UiState.Loading
            when (val result = collegeRepository.getDepartments(collegeId)) {
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        _departmentsState.value = UiState.Empty
                    } else {
                        _departmentsState.value = UiState.Success(result.data)
                    }
                }
                is Result.Error -> {
                    _departmentsState.value = UiState.Error(result.message, result.exception)
                }
                is Result.Loading -> {}
            }
        }
    }

    fun addDepartment(name: String, code: String, headName: String, collegeId: String) {
        viewModelScope.launch {
            val department = Department(
                id = UUID.randomUUID().toString(),
                collegeId = collegeId,
                name = name,
                code = code,
                headName = headName
            )
            collegeRepository.addDepartment(department)
            loadDepartments(collegeId)
        }
    }
}
