package com.example.campv.data.model

data class Department(
    val id: String = "",
    val collegeId: String = "",
    val name: String = "",
    val code: String = "",
    val headName: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
