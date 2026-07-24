package com.example.campv.data.model

data class Comment(
    val id: String = "",
    val demandId: String = "",
    val userId: String = "",
    val userName: String = "",
    val userRole: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
