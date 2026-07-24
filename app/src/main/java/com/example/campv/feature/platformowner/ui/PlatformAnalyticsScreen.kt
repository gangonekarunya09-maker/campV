package com.example.campv.feature.platformowner.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.campv.ui.components.AppCard
import com.example.campv.ui.components.AppTopBar

@Composable
fun PlatformAnalyticsScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = { AppTopBar(title = "Platform Analytics", onBackClick = onBackClick) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            AppCard {
                Text(text = "Global Platform Stats", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Total Partner Colleges: 42", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Total Active Students: 38,500", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Demands Resolved System-Wide: 14,200", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
