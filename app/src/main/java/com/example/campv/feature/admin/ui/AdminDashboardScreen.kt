package com.example.campv.feature.admin.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.campv.core.session.SessionManager
import com.example.campv.ui.components.AppButton
import com.example.campv.ui.components.AppCard
import com.example.campv.ui.components.AppTopBar

@Composable
fun AdminDashboardScreen(
    onManageDemandsClick: () -> Unit,
    onReportsClick: () -> Unit
) {
    val currentUser = SessionManager.currentUser.collectAsState().value

    Scaffold(
        topBar = { AppTopBar(title = "Admin Dashboard") }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Text(
                text = "Welcome, ${currentUser?.name ?: "Department Admin"}",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            AppCard {
                Text(text = "Quick Management Controls", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Review student demands, post updates, and view analytical reports for your campus.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            AppButton(
                text = "Manage Demands",
                onClick = onManageDemandsClick
            )
            Spacer(modifier = Modifier.height(16.dp))

            AppButton(
                text = "View Analytics & Reports",
                onClick = onReportsClick,
                isOutlined = true
            )
        }
    }
}
