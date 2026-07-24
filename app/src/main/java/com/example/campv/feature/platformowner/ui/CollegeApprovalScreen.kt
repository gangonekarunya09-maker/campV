package com.example.campv.feature.platformowner.ui

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
import com.example.campv.data.model.College
import com.example.campv.feature.platformowner.viewmodel.PlatformOwnerViewModel
import com.example.campv.feature.shared.ui.EmptyState
import com.example.campv.feature.shared.ui.ErrorScreen
import com.example.campv.feature.shared.ui.LoadingScreen
import com.example.campv.ui.components.AppButton
import com.example.campv.ui.components.AppCard
import com.example.campv.ui.components.AppTopBar

@Composable
fun CollegeApprovalScreen(
    onBackClick: () -> Unit,
    viewModel: PlatformOwnerViewModel = viewModel()
) {
    val collegesState by viewModel.collegesState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAllColleges()
    }

    Scaffold(
        topBar = { AppTopBar(title = "College Approvals", onBackClick = onBackClick) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            when (val state = collegesState) {
                is UiState.Loading -> LoadingScreen()
                is UiState.Empty -> EmptyState(message = "No colleges pending approval.")
                is UiState.Error -> ErrorScreen(message = state.message)
                is UiState.Success -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(state.data) { college ->
                            CollegeApprovalCard(
                                college = college,
                                onApprove = { viewModel.setCollegeApproval(college.id, true) },
                                onReject = { viewModel.setCollegeApproval(college.id, false) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CollegeApprovalCard(
    college: College,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    AppCard {
        Text(text = "${college.name} (${college.code})", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Domain: @${college.domain}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Principal: ${college.principalName} (${college.principalEmail})", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Status: ${if (college.isApproved) "APPROVED" else "PENDING"}", color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AppButton(
                text = "Approve",
                onClick = onApprove,
                modifier = Modifier.weight(1f)
            )
            AppButton(
                text = "Reject",
                onClick = onReject,
                isOutlined = true,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
