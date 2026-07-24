package com.example.campv.core.validation

import com.example.campv.core.constants.AppConstants

object PasswordValidator {
    fun isValid(password: String): Boolean {
        return password.length >= AppConstants.MIN_PASSWORD_LENGTH
    }
}
