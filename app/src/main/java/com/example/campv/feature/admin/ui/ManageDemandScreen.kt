package com.example.campv.feature.admin.ui

import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campv.core.common.UiState
import com.example.campv.core.session.SessionManager
import com.example.campv.data.model.Demand
import com.example.campv.feature.admin.components.AdminDemandCard
import com.example.campv.feature.admin.components.DemandStatusChip
import com.example.campv.feature.admin.viewmodel.AdminViewModel
import com.example.campv.feature.shared.ui.EmptyState
import com.example.campv.feature.shared.ui.ErrorScreen
import com.example.campv.feature.shared.ui.LoadingScreen
import com.example.campv.ui.components.AppTextField
import com.example.campv.ui.components.AppTopBar

@Composable
fun ManageDemandsScreen(
    onBackClick: () -> Unit,
    onDemandClick: (String) -> Unit = {},
    viewModel: AdminViewModel = viewModel()
) {
    val currentUser by SessionManager.currentUser.collectAsState()
    val demandsState by viewModel.demandsState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedStatus by viewModel.selectedStatus.collectAsState()

    var selectedDemandForAction by remember { mutableStateOf<Demand?>(null) }
    var targetStatusForAction by remember { mutableStateOf("") }
    var officialResponseText by remember { mutableStateOf("") }

    LaunchedEffect(currentUser?.collegeId) {
        currentUser?.collegeId?.let { viewModel.loadDemands(it) }
    }

    if (selectedDemandForAction != null) {
        AlertDialog(
            onDismissRequest = { selectedDemandForAction = null },
            title = { Text("Official Admin Response", style = MaterialTheme.typography.titleLarge) },
            text = {
                Column {
                    Text(
                        text = "Updating status to $targetStatusForAction for: ${selectedDemandForAction?.title}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    AppTextField(
                        value = officialResponseText,
                        onValueChange = { officialResponseText = it },
                        label = "Official Response / Note"
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val demand = selectedDemandForAction
                        val collegeId = currentUser?.collegeId
                        if (demand != null && collegeId != null) {
                            viewModel.updateStatus(demand.id, targetStatusForAction, officialResponseText, collegeId)
                        }
                        selectedDemandForAction = null
                        officialResponseText = ""
                    }
                ) {
                    Text("Submit")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedDemandForAction = null }) {
                    Text("Cancel")
                }
            }
        )
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Search Bar
            AppTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                label = "Search demands by title, student, or category..."
            )

            // Status Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filterList = listOf("ALL", "PENDING", "IN_PROGRESS", "RESOLVED", "REJECTED")
                filterList.forEach { statusOption ->
                    DemandStatusChip(
                        status = statusOption,
                        isSelected = selectedStatus.equals(statusOption, ignoreCase = true),
                        onClick = { viewModel.onStatusFilterSelected(statusOption) }
                    )
                }
            }

            // Demands List State
            when (val state = demandsState) {
                is UiState.Loading -> LoadingScreen()
                is UiState.Empty -> EmptyState(message = "No demands match the selected filter.")
                is UiState.Error -> ErrorScreen(message = state.message)
                is UiState.Success -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.data) { demand ->
                            AdminDemandCard(
                                demand = demand,
                                onCardClick = { onDemandClick(demand.id) },
                                onActionClick = { actionStatus ->
                                    selectedDemandForAction = demand
                                    targetStatusForAction = actionStatus
                                    officialResponseText = demand.adminResponse
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


