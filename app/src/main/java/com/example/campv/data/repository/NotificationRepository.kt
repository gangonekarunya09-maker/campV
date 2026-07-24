package com.example.campv.data.repository

import com.example.campv.core.common.Result
import com.example.campv.core.constants.FirebaseConstants
import com.example.campv.data.model.Notification
import com.example.campv.data.remote.FirestoreService

class NotificationRepository(
    private val firestoreService: FirestoreService = FirestoreService()
) {
    suspend fun sendNotification(notification: Notification): Result<Notification> {
        return try {
            firestoreService.setDocument(
                FirebaseConstants.COLLECTION_NOTIFICATIONS,
                notification.id,
                notification
            )
            Result.Success(notification)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getNotificationsForUser(userId: String): Result<List<Notification>> {
        return try {
            val list = firestoreService.queryCollection(
                FirebaseConstants.COLLECTION_NOTIFICATIONS,
                "userId",
                userId,
                Notification::class.java
            )
            Result.Success(list)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
