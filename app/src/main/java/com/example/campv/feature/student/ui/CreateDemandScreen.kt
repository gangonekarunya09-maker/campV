package com.example.campv.feature.student.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campv.core.session.SessionManager
import com.example.campv.feature.student.viewmodel.DemandViewModel
import com.example.campv.ui.components.AppButton
import com.example.campv.ui.components.AppTextField
import com.example.campv.ui.components.AppTopBar

@Composable
fun CreateDemandScreen(
    onBackClick: () -> Unit,
    onDemandCreated: () -> Unit,
    viewModel: DemandViewModel = viewModel()
) {
    val currentUser = SessionManager.currentUser.collectAsState().value
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Create New Demand",
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            AppTextField(
                value = title,
                onValueChange = { title = it },
                label = "Demand Title",
                placeholder = "e.g. Need more library books for CS"
            )
            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = category,
                onValueChange = { category = it },
                label = "Category",
                placeholder = "e.g. Infrastructure, Academic, Hostel"
            )
            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = description,
                onValueChange = { description = it },
                label = "Detailed Description",
                placeholder = "Explain why this demand is important...",
                singleLine = false
            )
            Spacer(modifier = Modifier.height(24.dp))

            AppButton(
                text = "Submit Demand",
                onClick = {
                    currentUser?.let { user ->
                        viewModel.createDemand(
                            title = title,
                            description = description,
                            category = category,
                            collegeId = user.collegeId,
                            departmentId = "",
                            studentId = user.id,
                            studentName = user.name,
                            onSuccess = {
                                onDemandCreated()
                            }
                        )
                    }
                }
            )
        }
    }
}
