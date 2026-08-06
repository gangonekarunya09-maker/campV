package com.example.campv.data.model

data class Admin(
    override val id: String = "",
    override val email: String = "",
    override val name: String = "",
    override val collegeId: String = "",
    override val profileImageUrl: String = "",
    override val createdAt: Long = System.currentTimeMillis(),
    override val departmentId: String = ""
) : User(
    id = id,
    email = email,
    name = name,
    role = "ADMIN",
    collegeId = collegeId,
    profileImageUrl = profileImageUrl,
    createdAt = createdAt
)
