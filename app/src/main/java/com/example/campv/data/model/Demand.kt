package com.example.campv.data.model

data class Demand(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val collegeId: String = "",
    val departmentId: String = "",
    val studentId: String = "",
    val studentName: String = "",
    val upvotesCount: Int = 0,
    val status: String = "PENDING", // PENDING, IN_PROGRESS, RESOLVED, REJECTED
    val adminResponse: String = "",
    val attachmentUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
