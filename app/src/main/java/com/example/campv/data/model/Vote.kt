package com.example.campv.data.model

data class Vote(
    val id: String = "",
    val demandId: String = "",
    val userId: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
