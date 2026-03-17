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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wahid.wurly.R

/**
 * Shown when the user denied location permission (including permanently denied cases).
 */
@Composable
fun PermissionDeniedScreen(
    onOpenSettings: () -> Unit,
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
            text = stringResource(R.string.permission_denied_title),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = stringResource(R.string.permission_denied_message),
            style = MaterialTheme.typography.bodyMedium
        )
        Button(onClick = onOpenSettings) {
            Text(text = stringResource(R.string.permission_open_settings))
        }
        Button(onClick = onDismiss) {
            Text(text = stringResource(R.string.permission_cancel))
        }
    }
}