package com.example.campv.core.utils

import com.example.campv.core.constants.AppConstants
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    fun formatTimestamp(timestamp: Long, pattern: String = AppConstants.DATE_FORMAT_DISPLAY): String {
        if (timestamp <= 0) return ""
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
