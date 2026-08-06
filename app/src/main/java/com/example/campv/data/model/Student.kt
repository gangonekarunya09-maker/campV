package com.example.campv.data.model

data class Student(
    override val id: String = "",
    override val email: String = "",
    override val name: String = "",
    override val collegeId: String = "",
    override val profileImageUrl: String = "",
    override val createdAt: Long = System.currentTimeMillis(),
    val studentId: String = "",
    override val departmentId: String = "",
    val yearOfStudy: Int = 1
) : User(
    id = id,
    email = email,
    name = name,
    role = "STUDENT",
    collegeId = collegeId,
    profileImageUrl = profileImageUrl,
    createdAt = createdAt
)
