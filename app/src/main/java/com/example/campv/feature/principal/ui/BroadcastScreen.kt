package com.example.campv.feature.principal.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.campv.ui.components.AppButton
import com.example.campv.ui.components.AppTextField
import com.example.campv.ui.components.AppTopBar

@Composable
fun BroadcastScreen(
    onBackClick: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Scaffold(
        topBar = { AppTopBar(title = "Campus Broadcast", onBackClick = onBackClick) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            AppTextField(value = title, onValueChange = { title = it }, label = "Notice Title")
            Spacer(modifier = Modifier.height(16.dp))
            AppTextField(
                value = message,
                onValueChange = { message = it },
                label = "Notice Announcement",
                singleLine = false
            )
            Spacer(modifier = Modifier.height(24.dp))
            AppButton(
                text = "Send Broadcast to All Students",
                onClick = onBackClick
            )
        }
    }
}
