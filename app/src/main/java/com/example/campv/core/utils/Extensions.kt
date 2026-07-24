package com.example.campv.core.utils

import java.util.Locale

fun String.capitalizeWords(): String {
    return this.split(" ")
        .joinToString(" ") { word ->
            word.lowercase(Locale.getDefault())
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }
}
