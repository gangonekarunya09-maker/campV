package com.example.campv.data.model

data class Notification(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val message: String = "",
    val type: String = "GENERAL", // GENERAL, DEMAND_UPDATE, ANNOUNCEMENT
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
