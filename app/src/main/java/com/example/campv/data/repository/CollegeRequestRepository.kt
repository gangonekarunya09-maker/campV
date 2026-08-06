package com.example.campv.data.repository

import com.example.campv.core.common.Result
import com.example.campv.core.constants.FirebaseConstants
import com.example.campv.data.model.CollegeRequest
import com.example.campv.data.remote.FirestoreService
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await




class CollegeRequestRepository(
    private val firestoreService: FirestoreService = FirestoreService()
) {

    /**
     * Submit a new college registration request.
     */
    suspend fun submitRequest(
        request: CollegeRequest
    ): Result<CollegeRequest> {

        return try {

            val documentRef = FirebaseFirestore
                .getInstance()
                .collection(FirebaseConstants.COLLECTION_COLLEGE_REQUESTS)
                .document()

            val requestWithId = request.copy(
                id = documentRef.id,
                status = "PENDING"
            )

            documentRef.set(requestWithId).await()

            println("CampV: Firestore write successful")

            Result.Success(requestWithId)

        } catch (e: Exception) {

            e.printStackTrace()

            println("CampV ERROR: ${e.message}")

            Result.Error(e)
        }
    }

    /**
     * Get a request by ID.
     */
    suspend fun getRequestById(
        requestId: String
    ): Result<CollegeRequest> {

        return try {

            val request = firestoreService.getDocument(
                FirebaseConstants.COLLECTION_COLLEGE_REQUESTS,
                requestId,
                CollegeRequest::class.java
            )

            if (request != null) {
                Result.Success(request)
            } else {
                Result.Error(Exception("College request not found"))
            }

        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Get all college requests.
     */
    suspend fun getAllRequests(): Result<List<CollegeRequest>> {

        return try {

            val requests = firestoreService.getCollection(
                FirebaseConstants.COLLECTION_COLLEGE_REQUESTS,
                CollegeRequest::class.java
            )

            Result.Success(requests)

        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Get requests by status.
     */
    suspend fun getRequestsByStatus(
        status: String
    ): Result<List<CollegeRequest>> {

        return try {

            val requests = firestoreService.queryCollection(
                FirebaseConstants.COLLECTION_COLLEGE_REQUESTS,
                "status",
                status,
                CollegeRequest::class.java
            )

            Result.Success(requests)

        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Update request status.
     */
    suspend fun updateRequestStatus(
        requestId: String,
        status: String,
        reviewedBy: String,
        rejectionReason: String? = null
    ): Result<Unit> {

        return try {

            firestoreService.updateDocument(
                FirebaseConstants.COLLECTION_COLLEGE_REQUESTS,
                requestId,
                mapOf(
                    "status" to status,
                    "reviewedBy" to reviewedBy,
                    "reviewedAt" to System.currentTimeMillis(),
                    "rejectionReason" to (rejectionReason ?: "")
                )
            )

            Result.Success(Unit)

        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Delete a request.
     */
    suspend fun deleteRequest(
        requestId: String
    ): Result<Unit> {

        return try {

            firestoreService.deleteDocument(
                FirebaseConstants.COLLECTION_COLLEGE_REQUESTS,
                requestId
            )

            Result.Success(Unit)

        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}