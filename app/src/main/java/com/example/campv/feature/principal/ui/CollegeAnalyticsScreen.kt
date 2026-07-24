package com.example.campv.feature.principal.ui

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
fun CollegeAnalyticsScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = { AppTopBar(title = "College Analytics", onBackClick = onBackClick) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            AppCard {
                Text(text = "Overall Campus Health", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Active Students: 1,240", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Total Demands Raised: 312", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Resolution Rate: 92%", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
