package com.example.campv.data.model

data class DemandStatistics(
    val totalDemands: Int = 0,
    val pendingDemands: Int = 0,
    val inProgressDemands: Int = 0,
    val resolvedDemands: Int = 0,
    val rejectedDemands: Int = 0,
    val resolutionRatePercentage: Int = 0,
    val averageUpvotes: Double = 0.0
)
