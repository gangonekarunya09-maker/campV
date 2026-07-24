package com.example.campv.data.repository

import com.example.campv.core.common.Result
import com.example.campv.core.constants.FirebaseConstants
import com.example.campv.data.model.Demand
import com.example.campv.data.model.Vote
import com.example.campv.data.remote.FirestoreService

class VoteRepository(
    private val firestoreService: FirestoreService = FirestoreService()
) {
    suspend fun upvoteDemand(demandId: String, userId: String): Result<Unit> {
        return try {
            val voteId = "${demandId}_${userId}"
            val vote = Vote(id = voteId, demandId = demandId, userId = userId)
            firestoreService.setDocument(FirebaseConstants.COLLECTION_VOTES, voteId, vote)

            // Increment upvote count on Demand
            val demand = firestoreService.getDocument(FirebaseConstants.COLLECTION_DEMANDS, demandId, Demand::class.java)
            if (demand != null) {
                firestoreService.setDocument(
                    FirebaseConstants.COLLECTION_DEMANDS,
                    demandId,
                    demand.copy(upvotesCount = demand.upvotesCount + 1)
                )
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun hasUserVoted(demandId: String, userId: String): Result<Boolean> {
        return try {
            val voteId = "${demandId}_${userId}"
            val vote = firestoreService.getDocument(FirebaseConstants.COLLECTION_VOTES, voteId, Vote::class.java)
            Result.Success(vote != null)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
