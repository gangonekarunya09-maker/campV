package com.example.campv.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campv.core.common.Result
import com.example.campv.core.session.SessionManager
import com.example.campv.core.validation.CollegeDomainValidator
import com.example.campv.core.validation.EmailValidator
import com.example.campv.core.validation.PasswordValidator
import com.example.campv.data.model.College
import com.example.campv.data.model.User
import com.example.campv.data.repository.AuthRepository
import com.example.campv.data.repository.CollegeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private val collegeRepository: CollegeRepository = CollegeRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, pass: String) {
        if (!EmailValidator.isValid(email)) {
            _uiState.value = AuthUiState.Idle
            _uiState.value = AuthUiState.Error("Please enter a valid email address.")
            return
        }
        if (!PasswordValidator.isValid(pass)) {
            _uiState.value = AuthUiState.Error("Password must be at least 6 characters.")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            when (val result = authRepository.login(email, pass)) {
                is Result.Success -> {
                    SessionManager.clearSession()
                    SessionManager.setCurrentUser(result.data)
                    _uiState.value = AuthUiState.Success(result.data)
                }
                is Result.Error -> {
                    _uiState.value = AuthUiState.Error(result.message)
                }
                is Result.Loading -> {}
            }
        }
    }

    fun registerCollege(
        collegeName: String,
        code: String,
        domain: String,
        address: String,
        principalName: String,
        principalEmail: String
    ) {
        if (collegeName.isBlank() || code.isBlank() || domain.isBlank() || principalEmail.isBlank()) {
            _uiState.value = AuthUiState.Error("All fields are required.")
            return
        }
        if (!EmailValidator.isValid(principalEmail)) {
            _uiState.value = AuthUiState.Error("Invalid principal email address.")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val college = College(
                id = UUID.randomUUID().toString(),
                name = collegeName,
                code = code,
                domain = domain,
                address = address,
                principalName = principalName,
                principalEmail = principalEmail,
                isApproved = false
            )
            when (val result = collegeRepository.registerCollege(college)) {
                is Result.Success -> {
                    _uiState.value = AuthUiState.Idle
                }
                is Result.Error -> {
                    _uiState.value = AuthUiState.Error(result.message)
                }
                is Result.Loading -> {}
            }
        }
    }

    fun forgotPassword(email: String, onSent: () -> Unit) {
        if (!EmailValidator.isValid(email)) {
            _uiState.value = AuthUiState.Error("Please enter a valid email address.")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            when (val result = authRepository.forgotPassword(email)) {
                is Result.Success -> {
                    _uiState.value = AuthUiState.Idle
                    onSent()
                }
                is Result.Error -> {
                    _uiState.value = AuthUiState.Error(result.message)
                }
                is Result.Loading -> {}
            }
        }
    }
}
