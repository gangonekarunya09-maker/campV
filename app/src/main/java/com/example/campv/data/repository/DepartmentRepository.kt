package com.example.campv.data.repository

import com.example.campv.core.common.Result
import com.example.campv.core.constants.FirebaseConstants
import com.example.campv.data.model.Department
import com.example.campv.data.remote.FirestoreService

class DepartmentRepository(
    private val firestoreService: FirestoreService = FirestoreService()
) {
    suspend fun createDepartment(department: Department): Result<Department> {
        return try {
            firestoreService.setDocument(FirebaseConstants.COLLECTION_DEPARTMENTS, department.id, department)
            Result.Success(department)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getDepartmentsByCollege(collegeId: String): Result<List<Department>> {
        return try {
            val list = firestoreService.queryCollection(
                FirebaseConstants.COLLECTION_DEPARTMENTS,
                "collegeId",
                collegeId,
                Department::class.java
            )
            Result.Success(list)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun deleteDepartment(departmentId: String): Result<Unit> {
        return try {
            firestoreService.deleteDocument(FirebaseConstants.COLLECTION_DEPARTMENTS, departmentId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
