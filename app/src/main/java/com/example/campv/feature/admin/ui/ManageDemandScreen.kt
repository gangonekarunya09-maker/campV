package com.example.campv.feature.admin.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campv.core.common.UiState
import com.example.campv.core.session.SessionManager
import com.example.campv.data.model.Demand
import com.example.campv.feature.admin.viewmodel.AdminViewModel
import com.example.campv.feature.shared.ui.EmptyState
import com.example.campv.feature.shared.ui.ErrorScreen
import com.example.campv.feature.shared.ui.LoadingScreen
import com.example.campv.ui.components.AppButton
import com.example.campv.ui.components.AppCard
import com.example.campv.ui.components.AppTopBar

@Composable
fun ManageDemandsScreen(
    onBackClick: () -> Unit,
    viewModel: AdminViewModel = viewModel()
) {
    val currentUser = SessionManager.currentUser.collectAsState().value
    val demandsState by viewModel.demandsState.collectAsState()

    LaunchedEffect(currentUser?.collegeId) {
        currentUser?.collegeId?.let { viewModel.loadDemands(it) }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Manage Campus Demands",
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            when (val state = demandsState) {
                is UiState.Loading -> LoadingScreen()
                is UiState.Empty -> EmptyState(message = "No pending demands to manage.")
                is UiState.Error -> ErrorScreen(message = state.message)
                is UiState.Success -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.data) { demand ->
                            AdminDemandCard(
                                demand = demand,
                                onUpdateStatus = { newStatus ->
                                    currentUser?.collegeId?.let { cid ->
                                        viewModel.updateStatus(demand.id, newStatus, "Status updated by admin", cid)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminDemandCard(
    demand: Demand,
    onUpdateStatus: (String) -> Unit
) {
    AppCard {
        Text(text = demand.title, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Student: ${demand.studentName} | Upvotes: ${demand.upvotesCount}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = demand.description, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AppButton(
                text = "Approve",
                onClick = { onUpdateStatus("IN_PROGRESS") },
                modifier = Modifier.weight(1f)
            )
            AppButton(
                text = "Resolve",
                onClick = { onUpdateStatus("RESOLVED") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}
