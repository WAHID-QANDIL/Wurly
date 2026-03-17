package com.wahid.wurly.presentation.screen.permission

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Shown when we should explain why location permission is needed before requesting again.
 */
@Composable
fun RationaleScreen(
    onGrant: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "We need your location to show local weather.",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Grant location permission to continue.",
            style = MaterialTheme.typography.bodyMedium
        )
        Button(onClick = onGrant) {
            Text(text = "Grant permission")
        }
        Button(onClick = onDismiss) {
            Text(text = "Not now")
        }
    }
}