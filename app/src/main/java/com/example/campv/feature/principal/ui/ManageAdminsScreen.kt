package com.example.campv.feature.principal.ui

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.campv.core.constants.AppConstants
import com.example.campv.core.session.SessionManager
import com.example.campv.feature.principal.viewmodel.PrincipalViewModel
import com.example.campv.feature.shared.ui.EmptyState
import com.example.campv.feature.shared.ui.ErrorScreen
import com.example.campv.feature.shared.ui.LoadingScreen
import com.example.campv.ui.components.AppCard
import com.example.campv.ui.components.AppTopBar

@Composable
fun ManageAdminsScreen(
    onBackClick: () -> Unit,
    viewModel: PrincipalViewModel = viewModel()
) {
    val currentUser = SessionManager.currentUser.collectAsState().value
    val usersState by viewModel.usersState.collectAsState()

    LaunchedEffect(currentUser?.collegeId) {
        currentUser?.collegeId?.let { viewModel.loadCollegeUsers(it) }
    }

    Scaffold(
        topBar = { AppTopBar(title = "Manage Department Admins", onBackClick = onBackClick) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            when (val state = usersState) {
                is UiState.Loading -> LoadingScreen()
                is UiState.Empty -> EmptyState(message = "No department admins found.")
                is UiState.Error -> ErrorScreen(message = state.message)
                is UiState.Success -> {
                    val admins = state.data.filter { it.role == AppConstants.ROLE_ADMIN }
                    if (admins.isEmpty()) {
                        EmptyState(message = "No department admins found.")
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(admins) { admin ->
                                AppCard {
                                    Text(text = admin.name, style = MaterialTheme.typography.titleLarge)
                                    Text(text = admin.email, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
