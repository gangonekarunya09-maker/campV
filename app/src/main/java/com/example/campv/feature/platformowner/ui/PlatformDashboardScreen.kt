package com.example.campv.feature.platformowner.ui

import androidx.compose.foundation.layout.Arrangement
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
import com.example.campv.ui.components.AppButton
import com.example.campv.ui.components.AppCard
import com.example.campv.ui.components.AppTopBar

@Composable
fun PlatformDashboardScreen(
    onCollegeApprovalClick: () -> Unit,
    onPlatformAnalyticsClick: () -> Unit,
    onPlatformSettingsClick: () -> Unit
) {
    Scaffold(
        topBar = { AppTopBar(title = "Platform Owner Dashboard") }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Multi-College Platform Control", style = MaterialTheme.typography.headlineMedium)

            AppCard {
                Text(text = "Platform Overview", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Approve new college registrations, view cross-college analytics, and manage global system settings.", style = MaterialTheme.typography.bodyMedium)
            }

            AppButton(text = "College Approvals", onClick = onCollegeApprovalClick)
            AppButton(text = "Platform Analytics", onClick = onPlatformAnalyticsClick)
            AppButton(text = "Platform Settings", onClick = onPlatformSettingsClick, isOutlined = true)
        }
    }
}
