package com.example.campv.data.mapper

import com.example.campv.core.constants.AppConstants
import com.example.campv.data.model.Admin
import com.example.campv.data.model.Principal
import com.example.campv.data.model.Student
import com.example.campv.data.model.User

object UserMapper {
    fun fromMap(id: String, map: Map<String, Any?>): User {
        val email = map["email"] as? String ?: ""
        val name = map["name"] as? String ?: ""
        val role = map["role"] as? String ?: AppConstants.ROLE_STUDENT
        val collegeId = map["collegeId"] as? String ?: ""
        val profileImageUrl = map["profileImageUrl"] as? String ?: ""
        val createdAt = (map["createdAt"] as? Long) ?: System.currentTimeMillis()

        return when (role) {
            AppConstants.ROLE_STUDENT -> Student(
                id = id,
                email = email,
                name = name,
                collegeId = collegeId,
                profileImageUrl = profileImageUrl,
                createdAt = createdAt,
                studentId = map["studentId"] as? String ?: "",
                departmentId = map["departmentId"] as? String ?: "",
                yearOfStudy = (map["yearOfStudy"] as? Long)?.toInt() ?: 1
            )
            AppConstants.ROLE_PRINCIPAL -> Principal(
                id = id,
                email = email,
                name = name,
                collegeId = collegeId,
                profileImageUrl = profileImageUrl,
                createdAt = createdAt,
                designation = map["designation"] as? String ?: "Principal"
            )
            AppConstants.ROLE_ADMIN -> Admin(
                id = id,
                email = email,
                name = name,
                collegeId = collegeId,
                profileImageUrl = profileImageUrl,
                createdAt = createdAt,
                departmentId = map["departmentId"] as? String ?: ""
            )
            else -> User(
                id = id,
                email = email,
                name = name,
                role = role,
                collegeId = collegeId,
                profileImageUrl = profileImageUrl,
                createdAt = createdAt
            )
        }
    }
}
