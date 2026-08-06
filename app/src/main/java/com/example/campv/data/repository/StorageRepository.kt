package com.example.campv.data.repository

import android.net.Uri
import com.example.campv.core.common.Result
import com.example.campv.data.remote.StorageService

class StorageRepository(
    private val storageService: StorageService = StorageService()
) {
    suspend fun uploadFile(path: String, fileUri: Uri): Result<String> {
        return try {
            val downloadUrl = storageService.uploadFile(path, fileUri)
            Result.Success(downloadUrl)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun deleteFile(path: String): Result<Unit> {
        return try {
            storageService.deleteFile(path)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
