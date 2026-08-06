package com.example.campv.feature.admin.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campv.core.session.SessionManager
import com.example.campv.feature.admin.components.AdminStatsCard
import com.example.campv.feature.admin.viewmodel.AdminViewModel
import com.example.campv.ui.components.AppButton
import com.example.campv.ui.components.AppCard
import com.example.campv.ui.components.AppTopBar

@Composable
fun AdminDashboardScreen(
    onManageDemandsClick: () -> Unit,
    onReportsClick: () -> Unit,
    viewModel: AdminViewModel = viewModel()
) {
    val currentUser by SessionManager.currentUser.collectAsState()
    val stats by viewModel.statsState.collectAsState()

    LaunchedEffect(currentUser?.collegeId) {
        currentUser?.collegeId?.let { viewModel.loadDemands(it) }
    }

    Scaffold(
        topBar = { AppTopBar(title = "Admin Dashboard") }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Welcome, ${currentUser?.name ?: "Department Admin"}",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Department: ${currentUser?.departmentId ?: "General"}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Statistics Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AdminStatsCard(
                    title = "Total Demands",
                    value = "${stats.totalCount}",
                    subtitle = "Campus-wide",
                    icon = Icons.Default.Assignment,
                    accentColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                AdminStatsCard(
                    title = "Pending",
                    value = "${stats.pendingCount}",
                    subtitle = "Action required",
                    icon = Icons.Default.HourglassEmpty,
                    accentColor = Color(0xFFE65100),
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AdminStatsCard(
                    title = "In Progress",
                    value = "${stats.inProgressCount}",
                    subtitle = "Under review",
                    icon = Icons.Default.PendingActions,
                    accentColor = Color(0xFF1565C0),
                    modifier = Modifier.weight(1f)
                )
                AdminStatsCard(
                    title = "Resolved",
                    value = "${stats.resolvedCount}",
                    subtitle = "${stats.resolutionRatePercentage}% success rate",
                    icon = Icons.Default.CheckCircle,
                    accentColor = Color(0xFF2E7D32),
                    modifier = Modifier.weight(1f)
                )
            }

            AppCard {
                Text(text = "Quick Management Controls", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Review student demands, track resolution metrics, and publish official responses for your campus.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            AppButton(
                text = "Manage Demands (${stats.pendingCount} Pending)",
                onClick = onManageDemandsClick
            )

            AppButton(
                text = "View Analytics & Reports",
                onClick = onReportsClick,
                isOutlined = true
            )
        }
    }
}

