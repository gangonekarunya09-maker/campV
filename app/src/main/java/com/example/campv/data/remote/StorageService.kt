package com.example.campv.data.remote

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class StorageService(
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
) {
    suspend fun uploadFile(path: String, fileUri: Uri): String {
        val ref = storage.reference.child(path)
        ref.putFile(fileUri).await()
        return ref.downloadUrl.await().toString()
    }

    suspend fun deleteFile(path: String) {
        storage.reference.child(path).delete().await()
    }
}
