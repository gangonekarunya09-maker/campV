package com.example.campv.data.repository

import com.example.campv.core.common.Result
import com.example.campv.core.constants.FirebaseConstants
import com.example.campv.data.model.Comment
import com.example.campv.data.remote.FirestoreService

class CommentRepository(
    private val firestoreService: FirestoreService = FirestoreService()
) {
    suspend fun addComment(comment: Comment): Result<Comment> {
        return try {
            firestoreService.setDocument(FirebaseConstants.COLLECTION_COMMENTS, comment.id, comment)
            Result.Success(comment)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getCommentsForDemand(demandId: String): Result<List<Comment>> {
        return try {
            val comments = firestoreService.queryCollection(
                FirebaseConstants.COLLECTION_COMMENTS,
                "demandId",
                demandId,
                Comment::class.java
            )
            Result.Success(comments)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
