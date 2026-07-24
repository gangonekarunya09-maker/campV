package com.example.campv.data.model

data class Principal(
    override val id: String = "",
    override val email: String = "",
    override val name: String = "",
    override val collegeId: String = "",
    override val profileImageUrl: String = "",
    override val createdAt: Long = System.currentTimeMillis(),
    val designation: String = "Principal"
) : User(
    id = id,
    email = email,
    name = name,
    role = "PRINCIPAL",
    collegeId = collegeId,
    profileImageUrl = profileImageUrl,
    createdAt = createdAt
)
