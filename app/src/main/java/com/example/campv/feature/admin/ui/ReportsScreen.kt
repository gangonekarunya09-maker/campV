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
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.campv.ui.components.AppCard
import com.example.campv.ui.components.AppTopBar

@Composable
fun ReportsScreen(
    onBackClick: () -> Unit,
    viewModel: AdminViewModel = viewModel()
) {
    val currentUser by SessionManager.currentUser.collectAsState()
    val stats by viewModel.statsState.collectAsState()

    LaunchedEffect(currentUser?.collegeId) {
        currentUser?.collegeId?.let { viewModel.loadDemands(it) }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Analytics & Reports",
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Overview Satisfaction Index Card
            AppCard {
                Text(text = "Campus Demand Satisfaction Index", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${stats.resolutionRatePercentage}% Resolution Rate",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { stats.resolutionRatePercentage / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${stats.resolvedCount} resolved out of ${stats.totalCount} total demands submitted",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Detailed Breakdown Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AdminStatsCard(
                    title = "Pending Demands",
                    value = "${stats.pendingCount}",
                    subtitle = "Awaiting response",
                    icon = Icons.Default.HourglassEmpty,
                    accentColor = Color(0xFFE65100),
                    modifier = Modifier.weight(1f)
                )
                AdminStatsCard(
                    title = "In Progress",
                    value = "${stats.inProgressCount}",
                    subtitle = "Being executed",
                    icon = Icons.Default.Assessment,
                    accentColor = Color(0xFF1565C0),
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AdminStatsCard(
                    title = "Resolved",
                    value = "${stats.resolvedCount}",
                    subtitle = "Completed demands",
                    icon = Icons.Default.CheckCircle,
                    accentColor = Color(0xFF2E7D32),
                    modifier = Modifier.weight(1f)
                )
                AdminStatsCard(
                    title = "Rejected",
                    value = "${stats.rejectedCount}",
                    subtitle = "Closed / invalid",
                    accentColor = Color(0xFFC62828),
                    modifier = Modifier.weight(1f)
                )
            }

            AppCard {
                Text(text = "Summary Insight", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Most active campus demands revolve around facilities and academic scheduling. Keep response times low to maintain student satisfaction.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

