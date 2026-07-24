package com.example.campv.data.repository

import com.example.campv.core.common.Result
import com.example.campv.core.constants.FirebaseConstants
import com.example.campv.data.model.User
import com.example.campv.data.remote.FirestoreService

class UserRepository(
    private val firestoreService: FirestoreService = FirestoreService()
) {
    suspend fun getUserProfile(userId: String): Result<User> {
        return try {
            val user = firestoreService.getDocument(
                FirebaseConstants.COLLECTION_USERS,
                userId,
                User::class.java
            ) ?: return Result.Error(Exception("User not found"))
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun updateUserProfile(user: User): Result<Unit> {
        return try {
            firestoreService.setDocument(FirebaseConstants.COLLECTION_USERS, user.id, user)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getUsersByCollege(collegeId: String): Result<List<User>> {
        return try {
            val users = firestoreService.queryCollection(
                FirebaseConstants.COLLECTION_USERS,
                "collegeId",
                collegeId,
                User::class.java
            )
            Result.Success(users)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
