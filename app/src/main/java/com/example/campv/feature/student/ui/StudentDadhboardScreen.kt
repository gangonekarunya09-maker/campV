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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.campv.data.model.Demand
import com.example.campv.feature.shared.ui.EmptyState
import com.example.campv.feature.shared.ui.ErrorScreen
import com.example.campv.feature.shared.ui.LoadingScreen
import com.example.campv.feature.student.viewmodel.StudentDashboardViewModel
import com.example.campv.ui.components.AppCard
import com.example.campv.ui.components.AppTopBar
import com.example.campv.ui.components.SearchBar

@Composable
fun StudentDashboardScreen(
    onCreateDemandClick: () -> Unit,
    onDemandClick: (demandId: String) -> Unit,
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    viewModel: StudentDashboardViewModel = viewModel()
) {
    val currentUser = SessionManager.currentUser.collectAsState().value
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(currentUser?.collegeId) {
        currentUser?.collegeId?.let { viewModel.loadDemands(it) }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Student Dashboard"
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateDemandClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Create Demand")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Welcome, ${currentUser?.name ?: "Student"}",
                    style = MaterialTheme.typography.titleLarge
                )
                Row {
                    IconButton(onClick = onNotificationsClick) {
                        Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                    IconButton(onClick = onProfileClick) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "Profile")
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                placeholder = "Search campus demands..."
            )
            Spacer(modifier = Modifier.height(16.dp))

            when (val state = uiState) {
                is UiState.Loading -> LoadingScreen()
                is UiState.Empty -> EmptyState(
                    message = "No demands created yet. Be the first to raise a demand!",
                    actionLabel = "Create Demand",
                    onActionClick = onCreateDemandClick
                )
                is UiState.Error -> ErrorScreen(
                    message = state.message,
                    onRetry = { currentUser?.collegeId?.let { viewModel.loadDemands(it) } }
                )
                is UiState.Success -> {
                    val filteredList = state.data.filter {
                        it.title.contains(searchQuery, ignoreCase = true) ||
                                it.category.contains(searchQuery, ignoreCase = true)
                    }
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredList) { demand ->
                            DemandItemCard(
                                demand = demand,
                                onClick = { onDemandClick(demand.id) },
                                onUpvote = {
                                    currentUser?.let { user ->
                                        viewModel.upvoteDemand(demand.id, user.id, user.collegeId)
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
private fun DemandItemCard(
    demand: Demand,
    onClick: () -> Unit,
    onUpvote: () -> Unit
) {
    AppCard(onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = demand.title,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = demand.status,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = demand.description,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "By ${demand.studentName}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            IconButton(onClick = onUpvote) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.ThumbUp, contentDescription = "Upvote")
                    Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                    Text(text = "${demand.upvotesCount}")
                }
            }
        }
    }
}
