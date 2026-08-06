package com.example.campv.feature.admin.viewmodel

data class DemandStatistics(
    val total: Int = 0,
    val pending: Int = 0,
    val approved: Int = 0,
    val rejected: Int = 0,
    val resolved: Int = 0
)