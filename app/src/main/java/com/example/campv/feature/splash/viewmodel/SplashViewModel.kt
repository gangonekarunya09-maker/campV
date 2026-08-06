package com.example.campv.feature.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campv.core.common.Result
import com.example.campv.core.session.SessionManager
import com.example.campv.data.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface SplashUiState {
    data object Loading : SplashUiState
    data object Login : SplashUiState
    data class Success(val role: String) : SplashUiState
}

class SplashViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            delay(1500)

            when (val result = authRepository.getCurrentUserProfile()) {

                is Result.Success -> {
                    SessionManager.setCurrentUser(result.data)
                    _uiState.value = SplashUiState.Success(result.data.role)
                }

                is Result.Error -> {


                    SessionManager.clearSession()
                    _uiState.value = SplashUiState.Login
                }

                is Result.Loading -> {}
            }
        }
    }
}