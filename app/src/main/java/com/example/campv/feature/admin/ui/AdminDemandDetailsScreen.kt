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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campv.core.common.UiState
import com.example.campv.core.session.SessionManager
import com.example.campv.data.model.Comment
import com.example.campv.data.model.Demand
import com.example.campv.feature.admin.components.DemandStatusChip
import com.example.campv.feature.admin.viewmodel.AdminDemandDetailsViewModel
import com.example.campv.feature.shared.ui.EmptyState
import com.example.campv.feature.shared.ui.ErrorScreen
import com.example.campv.feature.shared.ui.LoadingScreen
import com.example.campv.ui.components.AppButton
import com.example.campv.ui.components.AppCard
import com.example.campv.ui.components.AppTextField
import com.example.campv.ui.components.AppTopBar

@Composable
fun AdminDemandDetailsScreen(
    demandId: String,
    onBackClick: () -> Unit,
    viewModel: AdminDemandDetailsViewModel = viewModel()
) {
    val currentUser by SessionManager.currentUser.collectAsState()
    val demandState by viewModel.demandState.collectAsState()
    val commentsState by viewModel.commentsState.collectAsState()

    var showResponseDialog by remember { mutableStateOf(false) }
    var targetStatus by remember { mutableStateOf("") }
    var responseNoteText by remember { mutableStateOf("") }
    var commentInputText by remember { mutableStateOf("") }

    LaunchedEffect(demandId) {
        viewModel.loadDemandDetails(demandId)
    }

    if (showResponseDialog) {
        AlertDialog(
            onDismissRequest = { showResponseDialog = false },
            title = { Text("Update Demand Status", style = MaterialTheme.typography.titleLarge) },
            text = {
                Column {
                    Text("Set status to: $targetStatus", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(12.dp))
                    AppTextField(
                        value = responseNoteText,
                        onValueChange = { responseNoteText = it },
                        label = "Official Response Note"
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.updateDemandStatus(demandId, targetStatus, responseNoteText)
                        showResponseDialog = false
                        responseNoteText = ""
                    }
                ) {
                    Text("Submit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResponseDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Demand Management Details",
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
            when (val state = demandState) {
                is UiState.Loading -> LoadingScreen()
                is UiState.Error -> ErrorScreen(message = state.message)
                is UiState.Empty -> EmptyState(message = "Demand details not found.")
                is UiState.Success -> {
                    val demand = state.data
                    AdminDemandHeaderCard(
                        demand = demand,
                        onActionClick = { status ->
                            targetStatus = status
                            responseNoteText = demand.adminResponse
                            showResponseDialog = true
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Official Comments & Discussion", style = MaterialTheme.typography.titleMedium)

                    // Add Comment Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppTextField(
                            value = commentInputText,
                            onValueChange = { commentInputText = it },
                            label = "Write official response...",
                            modifier = Modifier.weight(1f)
                        )
                        AppButton(
                            text = "Post",
                            onClick = {
                                currentUser?.let { user ->
                                    viewModel.addOfficialComment(
                                        demandId = demandId,
                                        userId = user.id,
                                        userName = user.name,
                                        userRole = user.role,
                                        text = commentInputText
                                    )
                                    commentInputText = ""
                                }
                            },
                            modifier = Modifier.weight(0.4f)
                        )
                    }

                    // Comments List
                    when (val cState = commentsState) {
                        is UiState.Loading -> LoadingScreen()
                        is UiState.Empty -> EmptyState(message = "No comments yet.")
                        is UiState.Error -> ErrorScreen(message = cState.message)
                        is UiState.Success -> {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(cState.data) { comment ->
                                    AdminCommentCardItem(comment = comment)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminDemandHeaderCard(
    demand: Demand,
    onActionClick: (String) -> Unit
) {
    AppCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = demand.title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
            DemandStatusChip(status = demand.status)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Submitted by: ${demand.studentName} | Category: ${demand.category} | Upvotes: ${demand.upvotesCount}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = demand.description, style = MaterialTheme.typography.bodyLarge)

        if (demand.adminResponse.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Official Admin Response:\n${demand.adminResponse}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AppButton(
                text = "Approve",
                onClick = { onActionClick("IN_PROGRESS") },
                modifier = Modifier.weight(1f)
            )
            AppButton(
                text = "Resolve",
                onClick = { onActionClick("RESOLVED") },
                modifier = Modifier.weight(1f)
            )
            AppButton(
                text = "Reject",
                onClick = { onActionClick("REJECTED") },
                isOutlined = true,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun AdminCommentCardItem(comment: Comment) {
    AppCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = comment.userName, style = MaterialTheme.typography.titleSmall)
            Text(
                text = comment.userRole,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = comment.text, style = MaterialTheme.typography.bodyMedium)
    }
}
