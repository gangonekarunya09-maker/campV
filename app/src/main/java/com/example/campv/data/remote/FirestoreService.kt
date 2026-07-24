package com.example.campv.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreService(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun <T : Any> getDocument(collection: String, id: String, clazz: Class<T>): T? {
        val doc = firestore.collection(collection).document(id).get().await()
        return doc.toObject(clazz)
    }

    suspend fun setDocument(collection: String, id: String, data: Any) {
        firestore.collection(collection).document(id).set(data).await()
    }

    suspend fun <T : Any> getCollection(collection: String, clazz: Class<T>): List<T> {
        val query = firestore.collection(collection).get().await()
        return query.toObjects(clazz)
    }

    suspend fun <T : Any> queryCollection(
        collection: String,
        field: String,
        value: Any,
        clazz: Class<T>
    ): List<T> {
        val query = firestore.collection(collection).whereEqualTo(field, value).get().await()
        return query.toObjects(clazz)
    }

    suspend fun deleteDocument(collection: String, id: String) {
        firestore.collection(collection).document(id).delete().await()
    }
}
