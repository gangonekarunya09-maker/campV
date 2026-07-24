package com.example.campv.data.repository

import com.example.campv.core.common.Result
import com.example.campv.data.model.User
import com.example.campv.data.remote.FirebaseAuthService
import com.example.campv.data.remote.FirestoreService
import com.example.campv.core.constants.FirebaseConstants
import com.google.firebase.auth.FirebaseUser

class AuthRepository(
    private val authService: FirebaseAuthService = FirebaseAuthService(),
    private val firestoreService: FirestoreService = FirestoreService()
) {
    val currentFirebaseUser: FirebaseUser?
        get() = authService.currentUser

    suspend fun login(email: String, pass: String): Result<User> {
        return try {
            val fUser = authService.login(email, pass)
                ?: return Result.Error(Exception("Authentication failed"))
            val user = firestoreService.getDocument(
                FirebaseConstants.COLLECTION_USERS,
                fUser.uid,
                User::class.java
            ) ?: User(id = fUser.uid, email = email, name = fUser.displayName ?: email)
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun registerUser(user: User, pass: String): Result<User> {
        return try {
            val fUser = authService.register(user.email, pass)
                ?: return Result.Error(Exception("Registration failed"))
            val updatedUser = when (user) {
                is com.example.campv.data.model.Student -> user.copy(id = fUser.uid)
                is com.example.campv.data.model.Principal -> user.copy(id = fUser.uid)
                is com.example.campv.data.model.Admin -> user.copy(id = fUser.uid)
                else -> User(id = fUser.uid, email = user.email, name = user.name, role = user.role, collegeId = user.collegeId)
            }
            firestoreService.setDocument(FirebaseConstants.COLLECTION_USERS, fUser.uid, updatedUser)
            Result.Success(updatedUser)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun forgotPassword(email: String): Result<Unit> {
        return try {
            authService.sendPasswordResetEmail(email)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    fun logout() {
        authService.logout()
    }
}
