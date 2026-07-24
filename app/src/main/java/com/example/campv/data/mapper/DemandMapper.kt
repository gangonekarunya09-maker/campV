package com.example.campv.data.mapper

import com.example.campv.data.model.Demand

object DemandMapper {
    fun fromMap(id: String, map: Map<String, Any?>): Demand {
        return Demand(
            id = id,
            title = map["title"] as? String ?: "",
            description = map["description"] as? String ?: "",
            category = map["category"] as? String ?: "",
            collegeId = map["collegeId"] as? String ?: "",
            departmentId = map["departmentId"] as? String ?: "",
            studentId = map["studentId"] as? String ?: "",
            studentName = map["studentName"] as? String ?: "",
            upvotesCount = (map["upvotesCount"] as? Long)?.toInt() ?: 0,
            status = map["status"] as? String ?: "PENDING",
            adminResponse = map["adminResponse"] as? String ?: "",
            attachmentUrl = map["attachmentUrl"] as? String ?: "",
            createdAt = (map["createdAt"] as? Long) ?: System.currentTimeMillis()
        )
    }
}
