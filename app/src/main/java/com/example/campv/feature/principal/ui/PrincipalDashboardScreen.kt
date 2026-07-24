package com.example.campv.feature.principal.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.campv.core.session.SessionManager
import com.example.campv.ui.components.AppButton
import com.example.campv.ui.components.AppCard
import com.example.campv.ui.components.AppTopBar

@Composable
fun PrincipalDashboardScreen(
    onManageStudentsClick: () -> Unit,
    onManageAdminsClick: () -> Unit,
    onDepartmentsClick: () -> Unit,
    onCollegeAnalyticsClick: () -> Unit,
    onCollegeSettingsClick: () -> Unit,
    onBroadcastClick: () -> Unit
) {
    val currentUser = SessionManager.currentUser.collectAsState().value

    Scaffold(
        topBar = { AppTopBar(title = "Principal Dashboard") }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Welcome, Principal ${currentUser?.name ?: ""}",
                style = MaterialTheme.typography.headlineMedium
            )

            AppCard {
                Text(text = "College Administration Center", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Oversee departments, approve admins, monitor student engagement, and broadcast notices.", style = MaterialTheme.typography.bodyMedium)
            }

            AppButton(text = "Manage Students", onClick = onManageStudentsClick)
            AppButton(text = "Manage Admins", onClick = onManageAdminsClick)
            AppButton(text = "Departments", onClick = onDepartmentsClick)
            AppButton(text = "College Analytics", onClick = onCollegeAnalyticsClick)
            AppButton(text = "College Settings", onClick = onCollegeSettingsClick)
            AppButton(text = "Send Broadcast Notice", onClick = onBroadcastClick, isOutlined = true)
        }
    }
}
