package com.example.campv.data.model

data class College(
    val id: String = "",
    val name: String = "",
    val code: String = "",
    val domain: String = "",
    val address: String = "",
    val principalName: String = "",
    val principalEmail: String = "",
    val isApproved: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
