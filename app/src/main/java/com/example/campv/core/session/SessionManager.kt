package com.example.campv.core.session

import com.example.campv.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SessionManager {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    fun setCurrentUser(user: User?) {
        _currentUser.value = user
    }

    fun clearSession() {
        _currentUser.value = null
    }

    val isLoggedIn: Boolean
        get() = _currentUser.value != null

    val currentRole: String?
        get() = _currentUser.value?.role
}
