package com.example.campv.feature.principal.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.campv.core.constants.AppConstants
import com.example.campv.core.session.SessionManager
import com.example.campv.feature.principal.viewmodel.PrincipalViewModel
import com.example.campv.feature.shared.ui.EmptyState
import com.example.campv.feature.shared.ui.ErrorScreen
import com.example.campv.feature.shared.ui.LoadingScreen
import com.example.campv.ui.components.AppCard
import com.example.campv.ui.components.AppTopBar

@Composable
fun ManageStudentsScreen(
    onBackClick: () -> Unit,
    viewModel: PrincipalViewModel = viewModel()
) {
    val currentUser = SessionManager.currentUser.collectAsState().value
    val usersState by viewModel.usersState.collectAsState()

    LaunchedEffect(currentUser?.collegeId) {
        currentUser?.collegeId?.let { viewModel.loadCollegeUsers(it) }
    }

    Scaffold(
        topBar = { AppTopBar(title = "Manage Students", onBackClick = onBackClick) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            when (val state = usersState) {
                is UiState.Loading -> LoadingScreen()
                is UiState.Empty -> EmptyState(message = "No registered students found.")
                is UiState.Error -> ErrorScreen(message = state.message)
                is UiState.Success -> {
                    val students = state.data.filter { it.role == AppConstants.ROLE_STUDENT }
                    if (students.isEmpty()) {
                        EmptyState(message = "No registered students found.")
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(students) { student ->
                                AppCard {
                                    Text(text = student.name, style = MaterialTheme.typography.titleLarge)
                                    Text(text = student.email, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
