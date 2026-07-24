package com.example.campv.data.repository

import com.example.campv.core.common.Result
import com.example.campv.core.constants.FirebaseConstants
import com.example.campv.data.model.Demand
import com.example.campv.data.remote.FirestoreService

class DemandRepository(
    private val firestoreService: FirestoreService = FirestoreService()
) {
    suspend fun createDemand(demand: Demand): Result<Demand> {
        return try {
            firestoreService.setDocument(FirebaseConstants.COLLECTION_DEMANDS, demand.id, demand)
            Result.Success(demand)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getDemandsByCollege(collegeId: String): Result<List<Demand>> {
        return try {
            val demands = firestoreService.queryCollection(
                FirebaseConstants.COLLECTION_DEMANDS,
                "collegeId",
                collegeId,
                Demand::class.java
            )
            Result.Success(demands)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getDemandById(demandId: String): Result<Demand> {
        return try {
            val demand = firestoreService.getDocument(
                FirebaseConstants.COLLECTION_DEMANDS,
                demandId,
                Demand::class.java
            ) ?: return Result.Error(Exception("Demand not found"))
            Result.Success(demand)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun updateDemandStatus(demandId: String, status: String, adminResponse: String): Result<Unit> {
        return try {
            val demand = firestoreService.getDocument(FirebaseConstants.COLLECTION_DEMANDS, demandId, Demand::class.java)
            if (demand != null) {
                firestoreService.setDocument(
                    FirebaseConstants.COLLECTION_DEMANDS,
                    demandId,
                    demand.copy(status = status, adminResponse = adminResponse)
                )
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
