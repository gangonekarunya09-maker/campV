package com.example.campv.feature.auth.ui

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campv.feature.auth.viewmodel.AuthUiState
import com.example.campv.feature.auth.viewmodel.AuthViewModel
import com.example.campv.ui.components.AppButton
import com.example.campv.ui.components.AppTextField
import com.example.campv.ui.components.AppTopBar

@Composable
fun RegisterCollegeScreen(
    onBackClick: () -> Unit,
    onSubmitSuccess: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var collegeName by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var domain by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var principalName by remember { mutableStateOf("") }
    var principalEmail by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Register College",
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Apply for College Onboarding",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = collegeName,
                onValueChange = { collegeName = it },
                label = "College Full Name"
            )
            Spacer(modifier = Modifier.height(12.dp))

            AppTextField(
                value = code,
                onValueChange = { code = it },
                label = "College Code (e.g. MIT, Stanford)"
            )
            Spacer(modifier = Modifier.height(12.dp))

            AppTextField(
                value = domain,
                onValueChange = { domain = it },
                label = "Email Domain (e.g. college.edu)"
            )
            Spacer(modifier = Modifier.height(12.dp))

            AppTextField(
                value = address,
                onValueChange = { address = it },
                label = "College Address",
                singleLine = false
            )
            Spacer(modifier = Modifier.height(12.dp))

            AppTextField(
                value = principalName,
                onValueChange = { principalName = it },
                label = "Principal / Dean Name"
            )
            Spacer(modifier = Modifier.height(12.dp))

            AppTextField(
                value = principalEmail,
                onValueChange = { principalEmail = it },
                label = "Principal Official Email"
            )
            Spacer(modifier = Modifier.height(24.dp))

            if (uiState is AuthUiState.Error) {
                Text(
                    text = (uiState as AuthUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            AppButton(
                text = "Submit Application",
                onClick = {
                    viewModel.registerCollege(
                        collegeName = collegeName.trim(),
                        code = code.trim().uppercase(),
                        domain = domain.trim().lowercase(),
                        address = address.trim(),
                        principalName = principalName.trim(),
                        principalEmail = principalEmail.trim()
                    )

                }
            )
        }
    }
}
