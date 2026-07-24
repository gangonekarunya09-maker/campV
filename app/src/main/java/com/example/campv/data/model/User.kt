package com.example.campv.data.model

open class User(
    open val id: String = "",
    open val email: String = "",
    open val name: String = "",
    open val role: String = "",
    open val collegeId: String = "",
    open val profileImageUrl: String = "",
    open val createdAt: Long = System.currentTimeMillis()
)
