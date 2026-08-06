package com.example.campv.feature.admin.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.campv.data.model.Demand
import com.example.campv.ui.components.AppButton
import com.example.campv.ui.components.AppCard

@Composable
fun AdminDemandCard(
    demand: Demand,
    onCardClick: () -> Unit,
    onActionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AppCard(
        modifier = modifier.clickable { onCardClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = demand.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            DemandStatusChip(status = demand.status)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Student: ${demand.studentName} | Category: ${demand.category} | Upvotes: ${demand.upvotesCount}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = demand.description,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 3
        )

        if (demand.adminResponse.isNotBlank()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Response: ${demand.adminResponse}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AppButton(
                text = "Approve",
                onClick = { onActionClick("IN_PROGRESS") },
                modifier = Modifier.weight(1f)
            )
            AppButton(
                text = "Resolve",
                onClick = { onActionClick("RESOLVED") },
                modifier = Modifier.weight(1f)
            )
            AppButton(
                text = "Reject",
                onClick = { onActionClick("REJECTED") },
                isOutlined = true,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
