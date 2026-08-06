package com.example.campv.feature.splash.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campv.core.constants.AppConstants
import com.example.campv.feature.splash.viewmodel.SplashUiState
import com.example.campv.feature.splash.viewmodel.SplashViewModel
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToDashboard: (String) -> Unit,
    viewModel: SplashViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // --- Animation State ---
    val titleAlpha   = remember { Animatable(0f) }
    val titleScale   = remember { Animatable(0.75f) }
    val taglineAlpha = remember { Animatable(0f) }
    val dividerAlpha = remember { Animatable(0f) }

    // --- Navigation side-effect ---
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            SplashUiState.Login       -> onNavigateToLogin()
            is SplashUiState.Success  -> onNavigateToDashboard(state.role)
            SplashUiState.Loading     -> Unit
        }
    }

    // --- Staggered entrance animations ---
    LaunchedEffect(Unit) {
        // Title: scale up + fade in
        launch {
            titleScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
            )
        }
        launch {
            titleAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 700)
            )
        }
        // Divider: fades in after 300ms
        launch {
            kotlinx.coroutines.delay(300)
            dividerAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 500)
            )
        }
        // Tagline: fades in after 600ms
        launch {
            kotlinx.coroutines.delay(600)
            taglineAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 600)
            )
        }
    }

    // --- Background gradient ---
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.background
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBrush),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 40.dp)
        ) {
            // App Name
            Text(
                text = AppConstants.APP_NAME,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 52.sp,
                    letterSpacing = 4.sp
                ),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .alpha(titleAlpha.value)
                    .scale(titleScale.value)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Thin accent divider
            Box(
                modifier = Modifier
                    .height(2.dp)
                    .padding(horizontal = 12.dp)
                    .alpha(dividerAlpha.value)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0f),
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0f)
                            )
                        )
                    )
            ) {
                Spacer(modifier = Modifier.fillMaxSize())
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tagline
            Text(
                text = "Student Demand App",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Light,
                    fontSize = 13.sp,
                    letterSpacing = 2.5.sp
                ),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(taglineAlpha.value)
            )
        }
    }
}
