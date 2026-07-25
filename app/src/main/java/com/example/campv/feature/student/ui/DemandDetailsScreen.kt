package com.example.campv.feature.student.ui

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
import com.example.campv.feature.shared.ui.EmptyState
import com.example.campv.feature.shared.ui.ErrorScreen
import com.example.campv.feature.shared.ui.LoadingScreen
import com.example.campv.feature.student.viewmodel.DemandViewModel
import com.example.campv.ui.components.AppButton
import com.example.campv.ui.components.AppCard
import com.example.campv.ui.components.AppTextField
import com.example.campv.ui.components.AppTopBar

@Composable
fun DemandDetailsScreen(
    demandId: String,
    onBackClick: () -> Unit,
    viewModel: DemandViewModel = viewModel()
) {
    val currentUser = SessionManager.currentUser.collectAsState().value
    val demandState by viewModel.demandState.collectAsState()
    val commentsState by viewModel.commentsState.collectAsState()
    var commentText by remember { mutableStateOf("") }

    LaunchedEffect(demandId) {
        viewModel.loadDemandDetails(demandId)
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Demand Details",
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
            when (val state = demandState) {
                is UiState.Loading -> LoadingScreen()
                is UiState.Error -> ErrorScreen(
                    message = state.message,
                    onRetry = {
                        viewModel.loadDemandDetails(demandId)
                    }
                )
                is UiState.Success -> {
                    val demand = state.data
                    AppCard {
                        Text(text = demand.title, style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Category: ${demand.category}", style = MaterialTheme.typography.labelLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = demand.description, style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Status: ${demand.status}", color = MaterialTheme.colorScheme.primary)
                        if (demand.adminResponse.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Admin Response: ${demand.adminResponse}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Comments & Discussion", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppTextField(
                            value = commentText,
                            onValueChange = { commentText = it },
                            label = "Add a comment",
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                        AppButton(
                            text = "Post",
                            onClick = {
                                currentUser?.let { user ->
                                    viewModel.addComment(
                                        demandId = demandId,
                                        userId = user.id,
                                        userName = user.name,
                                        userRole = user.role,
                                        text = commentText
                                    )
                                    commentText = ""
                                }
                            },
                            modifier = Modifier.height(56.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    when (val cState = commentsState) {
                        is UiState.Loading -> LoadingScreen()
                        is UiState.Empty -> EmptyState(message = "No comments yet.")
                        is UiState.Error -> ErrorScreen(message = cState.message)
                        is UiState.Success -> {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(cState.data) { comment ->
                                    CommentItem(comment = comment)
                                }
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun CommentItem(comment: Comment) {
    AppCard {
        Text(text = "${comment.userName} (${comment.userRole})", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = comment.text, style = MaterialTheme.typography.bodyMedium)
    }
}
