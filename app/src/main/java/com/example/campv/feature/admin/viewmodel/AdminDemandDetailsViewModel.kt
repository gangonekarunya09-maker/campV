package com.example.campv.feature.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campv.core.common.Result
import com.example.campv.core.common.UiState
import com.example.campv.data.model.Comment
import com.example.campv.data.model.Demand
import com.example.campv.data.repository.CommentRepository
import com.example.campv.data.repository.DemandRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class AdminDemandDetailsViewModel(
    private val demandRepository: DemandRepository = DemandRepository(),
    private val commentRepository: CommentRepository = CommentRepository()
) : ViewModel() {

    private val _demandState = MutableStateFlow<UiState<Demand>>(UiState.Loading)
    val demandState: StateFlow<UiState<Demand>> = _demandState.asStateFlow()

    private val _commentsState = MutableStateFlow<UiState<List<Comment>>>(UiState.Loading)
    val commentsState: StateFlow<UiState<List<Comment>>> = _commentsState.asStateFlow()

    fun loadDemandDetails(demandId: String) {
        viewModelScope.launch {
            _demandState.value = UiState.Loading
            when (val result = demandRepository.getDemandById(demandId)) {
                is Result.Success -> {
                    _demandState.value = UiState.Success(result.data)
                }
                is Result.Error -> {
                    _demandState.value = UiState.Error(result.message, result.exception)
                }
                is Result.Loading -> {}
            }
            loadComments(demandId)
        }
    }

    fun loadComments(demandId: String) {
        viewModelScope.launch {
            _commentsState.value = UiState.Loading
            when (val result = commentRepository.getCommentsForDemand(demandId)) {
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        _commentsState.value = UiState.Empty
                    } else {
                        _commentsState.value = UiState.Success(result.data)
                    }
                }
                is Result.Error -> {
                    _commentsState.value = UiState.Error(result.message, result.exception)
                }
                is Result.Loading -> {}
            }
        }
    }

    fun updateDemandStatus(demandId: String, status: String, adminResponse: String) {
        viewModelScope.launch {
            when (demandRepository.updateDemandStatus(demandId, status, adminResponse)) {
                is Result.Success -> loadDemandDetails(demandId)
                else -> {}
            }
        }
    }

    fun addOfficialComment(
        demandId: String,
        userId: String,
        userName: String,
        userRole: String,
        text: String
    ) {
        if (text.isBlank()) return
        viewModelScope.launch {
            val comment = Comment(
                id = UUID.randomUUID().toString(),
                demandId = demandId,
                userId = userId,
                userName = userName,
                userRole = userRole,
                text = text
            )
            when (commentRepository.addComment(comment)) {
                is Result.Success -> loadComments(demandId)
                else -> {}
            }
        }
    }
}
