package com.example.campv.data.repository

import com.example.campv.core.common.Result
import com.example.campv.core.constants.FirebaseConstants
import com.example.campv.data.model.College
import com.example.campv.data.model.Department
import com.example.campv.data.remote.FirestoreService

class CollegeRepository(
    private val firestoreService: FirestoreService = FirestoreService()
) {
    suspend fun registerCollege(college: College): Result<College> {
        return try {
            firestoreService.setDocument(FirebaseConstants.COLLECTION_COLLEGES, college.id, college)
            Result.Success(college)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getCollegeById(collegeId: String): Result<College> {
        return try {
            val college = firestoreService.getDocument(
                FirebaseConstants.COLLECTION_COLLEGES,
                collegeId,
                College::class.java
            ) ?: return Result.Error(Exception("College not found"))
            Result.Success(college)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getAllColleges(): Result<List<College>> {
        return try {
            val colleges = firestoreService.getCollection(
                FirebaseConstants.COLLECTION_COLLEGES,
                College::class.java
            )
            Result.Success(colleges)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun updateCollegeStatus(collegeId: String, isApproved: Boolean): Result<Unit> {
        return try {
            val college = firestoreService.getDocument(FirebaseConstants.COLLECTION_COLLEGES, collegeId, College::class.java)
            if (college != null) {
                firestoreService.setDocument(
                    FirebaseConstants.COLLECTION_COLLEGES,
                    collegeId,
                    college.copy(isApproved = isApproved)
                )
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getDepartments(collegeId: String): Result<List<Department>> {
        return try {
            val departments = firestoreService.queryCollection(
                FirebaseConstants.COLLECTION_DEPARTMENTS,
                "collegeId",
                collegeId,
                Department::class.java
            )
            Result.Success(departments)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun addDepartment(department: Department): Result<Department> {
        return try {
            firestoreService.setDocument(FirebaseConstants.COLLECTION_DEPARTMENTS, department.id, department)
            Result.Success(department)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
