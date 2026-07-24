package com.example.campv.core.validation

object CollegeDomainValidator {
    fun isValidDomain(email: String, allowedDomain: String): Boolean {
        if (allowedDomain.isBlank()) return true
        val parts = email.split("@")
        if (parts.size != 2) return false
        val domain = parts[1].lowercase()
        val expected = allowedDomain.lowercase().removePrefix("@")
        return domain == expected || domain.endsWith(".$expected")
    }
}
