package com.example.campv.data.model

open class User(
    open val id: String = "",
    open val email: String = "",
    open val name: String = "",
    open val role: String = "",
    open val collegeId: String = "",
    open val departmentId: String = "",
    open val approved: Boolean = true,
    open val active: Boolean = true,
    open val profileImageUrl: String = "",
    open val createdAt: Long = System.currentTimeMillis()
)
