package com.example.campv.feature.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DemandStatusChip(
    status: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val (backgroundColor, textColor) = when (status.uppercase()) {
        "PENDING" -> Pair(Color(0xFFFFF3E0), Color(0xFFE65100))
        "IN_PROGRESS" -> Pair(Color(0xFFE3F2FD), Color(0xFF1565C0))
        "RESOLVED" -> Pair(Color(0xFFE8F5E9), Color(0xFF2E7D32))
        "REJECTED" -> Pair(Color(0xFFFFEBEE), Color(0xFFC62828))
        "ALL" -> Pair(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant)
        else -> Pair(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant)
    }

    val displayLabel = when (status.uppercase()) {
        "IN_PROGRESS" -> "In Progress"
        "PENDING" -> "Pending"
        "RESOLVED" -> "Resolved"
        "REJECTED" -> "Rejected"
        "ALL" -> "All"
        else -> status
    }

    val shape = RoundedCornerShape(20.dp)
    val boxModifier = modifier
        .clip(shape)
        .background(if (isSelected) textColor else backgroundColor)
        .then(
            if (isSelected) Modifier else Modifier.border(1.dp, textColor.copy(alpha = 0.3f), shape)
        )
        .then(
            if (onClick != null) Modifier.clickable { onClick() } else Modifier
        )
        .padding(horizontal = 12.dp, vertical = 6.dp)

    Box(modifier = boxModifier) {
        Text(
            text = displayLabel,
            color = if (isSelected) Color.White else textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
