package com.example.campv.data.model

data class DepartmentStatistics(
    val departmentId: String = "",
    val departmentName: String = "",
    val totalDemands: Int = 0,
    val resolvedDemands: Int = 0,
    val pendingDemands: Int = 0
)
