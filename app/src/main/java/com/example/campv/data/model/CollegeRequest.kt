package com.example.campv.data.model

data class CollegeRequest(
    val id: String = "",
    val collegeName: String = "",
    val collegeCode: String = "",
    val collegeDomain: String = "",
    val principalName: String = "",
    val principalEmail: String = "",
    val address: String = "",
    val phone: String = "",
    val status: String = "PENDING",
    val submittedAt: Long = System.currentTimeMillis(),
    val reviewedBy: String? = null,
    val reviewedAt: Long? = null,
    val rejectionReason: String? = null
)