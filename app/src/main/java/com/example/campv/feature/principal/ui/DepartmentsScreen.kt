package com.example.campv.feature.principal.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campv.core.common.UiState
import com.example.campv.core.session.SessionManager
import com.example.campv.feature.principal.viewmodel.PrincipalViewModel
import com.example.campv.feature.shared.ui.EmptyState
import com.example.campv.feature.shared.ui.ErrorScreen
import com.example.campv.feature.shared.ui.LoadingScreen
import com.example.campv.ui.components.AppButton
import com.example.campv.ui.components.AppCard
import com.example.campv.ui.components.AppTextField
import com.example.campv.ui.components.AppTopBar
import androidx.compose.foundation.lazy.items

@Composable
fun DepartmentsScreen(
    onBackClick: () -> Unit,
    viewModel: PrincipalViewModel = viewModel()
) {
    val currentUser = SessionManager.currentUser.collectAsState().value
    val departmentsState by viewModel.departmentsState.collectAsState()

    var name by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var headName by remember { mutableStateOf("") }

    LaunchedEffect(currentUser?.collegeId) {
        currentUser?.collegeId?.let { viewModel.loadDepartments(it) }
    }

    Scaffold(
        topBar = { AppTopBar(title = "College Departments", onBackClick = onBackClick) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            AppCard {
                Text(text = "Add New Department", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                AppTextField(value = name, onValueChange = { name = it }, label = "Department Name")
                Spacer(modifier = Modifier.height(6.dp))
                AppTextField(value = code, onValueChange = { code = it }, label = "Department Code")
                Spacer(modifier = Modifier.height(6.dp))
                AppTextField(value = headName, onValueChange = { headName = it }, label = "Head of Department")
                Spacer(modifier = Modifier.height(12.dp))
                AppButton(
                    text = "Add Department",
                    onClick = {
                        currentUser?.collegeId?.let { cid ->
                            viewModel.addDepartment(name, code, headName, cid)
                            name = ""
                            code = ""
                            headName = ""
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (val state = departmentsState) {
                is UiState.Loading -> LoadingScreen()
                is UiState.Empty -> EmptyState(message = "No departments created yet.")
                is UiState.Error -> ErrorScreen(message = state.message)
                is UiState.Success -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(state.data) { dept ->
                            AppCard {
                                Text(text = "${dept.name} (${dept.code})", style = MaterialTheme.typography.titleLarge)
                                Text(text = "HOD: ${dept.headName}", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}
