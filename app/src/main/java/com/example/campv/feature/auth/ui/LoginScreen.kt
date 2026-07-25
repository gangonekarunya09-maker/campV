package com.example.campv.feature.auth.ui

import androidx.compose.runtime.LaunchedEffect
import com.example.campv.data.model.User
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campv.core.constants.AppConstants
import com.example.campv.feature.auth.viewmodel.AuthUiState
import com.example.campv.feature.auth.viewmodel.AuthViewModel
import com.example.campv.feature.shared.ui.LoadingScreen
import com.example.campv.ui.components.AppButton
import com.example.campv.ui.components.AppTextField
import com.example.campv.ui.components.AppTopBar

@Composable


fun LoginScreen(
    onLoginSuccess: (User) -> Unit,
    onRegisterCollegeClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    viewModel: AuthViewModel = viewModel()
)  {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    val errorMessage = (uiState as? AuthUiState.Error)?.message
    val isLoading = uiState is AuthUiState.Loading

    Scaffold(
        topBar = { AppTopBar(title = "Sign In to ${AppConstants.APP_NAME}") }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(24.dp))

            AppTextField(
                value = email,
                onValueChange = { email = it.trimStart() },
                label = "College Email",
                placeholder = "user@college.edu"
            )
            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = onForgotPasswordClick,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Forgot Password?")
            }

            Spacer(modifier = Modifier.height(16.dp))
            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (isLoading) {
                LoadingScreen()
            } else {
                AppButton(
                    text = "Sign In",
                    onClick = {
                        viewModel.login(
                            email = email.trim(),
                            pass = password
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            AppButton(
                text = "Register Your College",
                onClick = onRegisterCollegeClick,
                isOutlined = true
            )
        }
    }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is AuthUiState.Success -> onLoginSuccess(state.user)
            else -> Unit
        }
    }
}
