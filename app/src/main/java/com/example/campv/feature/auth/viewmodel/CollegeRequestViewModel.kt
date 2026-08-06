package com.example.campv.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campv.core.common.Result
import com.example.campv.data.model.CollegeRequest
import com.example.campv.data.repository.CollegeRequestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CollegeRequestViewModel(
    private val repository: CollegeRequestRepository = CollegeRequestRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<CollegeRequestUiState>(CollegeRequestUiState.Idle)

    val uiState: StateFlow<CollegeRequestUiState> =
        _uiState.asStateFlow()

    fun submitRequest(
        collegeName: String,
        collegeCode: String,
        collegeDomain: String,
        address: String,
        principalName: String,
        principalEmail: String,
        phone: String
    ) {

        if (!validateInput(
                collegeName,
                collegeCode,
                collegeDomain,
                address,


                principalName,
                principalEmail,
                phone
            )
        ) {
            return
        }

        viewModelScope.launch {

            _uiState.value = CollegeRequestUiState.Loading

            val request = CollegeRequest(
                collegeName = collegeName,
                collegeCode = collegeCode.uppercase(),
                collegeDomain = collegeDomain.lowercase(),
                address = address,
                principalName = principalName,
                principalEmail = principalEmail.lowercase(),
                phone = phone
            )

            when (val result = repository.submitRequest(request)) {

                is Result.Success -> {
                    _uiState.value =
                        CollegeRequestUiState.Success()
                }

                is Result.Error -> {
                    _uiState.value =
                        CollegeRequestUiState.Error(
                            result.message
                        )
                }

                Result.Loading -> {
                    _uiState.value =
                        CollegeRequestUiState.Loading
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = CollegeRequestUiState.Idle
    }

    private fun validateInput(
        collegeName: String,
        collegeCode: String,
        collegeDomain: String,
        address: String,
        principalName: String,
        principalEmail: String,
        phone: String
    ): Boolean {

        when {

            collegeName.isBlank() -> {
                _uiState.value =
                    CollegeRequestUiState.Error(
                        "College name is required."
                    )
                return false
            }

            collegeCode.isBlank() -> {
                _uiState.value =
                    CollegeRequestUiState.Error(
                        "College code is required."
                    )
                return false
            }

            collegeDomain.isBlank() -> {
                _uiState.value =
                    CollegeRequestUiState.Error(
                        "College domain is required."
                    )
                return false
            }

            address.isBlank() -> {
                _uiState.value =
                    CollegeRequestUiState.Error(
                        "Address is required."
                    )
                return false
            }

            principalName.isBlank() -> {
                _uiState.value =
                    CollegeRequestUiState.Error(
                        "Principal name is required."
                    )
                return false
            }

            principalEmail.isBlank() -> {
                _uiState.value =
                    CollegeRequestUiState.Error(
                        "Principal email is required."
                    )
                return false
            }

            phone.isBlank() -> {
                _uiState.value =
                    CollegeRequestUiState.Error(
                        "Phone number is required."
                    )
                return false
            }
        }

        return true
    }
}