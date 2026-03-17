package com.wahid.wurly.presentation.screen.home

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wahid.wurly.presentation.screen.permission.PermissionDeniedScreen
import com.wahid.wurly.presentation.screen.permission.PermissionGateState
import com.wahid.wurly.presentation.screen.permission.RationaleScreen


@Composable
fun HomeEntry(
    onNavigateToForecast: () -> Unit
) {
    val context = LocalContext.current
    val activity = context.findActivity()

    var permissionState by remember { mutableStateOf(PermissionGateState.Default) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val allGranted = result.values.all { it }
        permissionState = when {
            allGranted -> PermissionGateState.Granted
            activity?.shouldShowLocationRationale() == true -> PermissionGateState.DeniedWithRationale
            else -> PermissionGateState.DeniedPermanently
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    when (permissionState) {
        PermissionGateState.Granted -> {
            val viewModel = hiltViewModel<HomeViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            HomeScreen(uiState = uiState, onEvent = viewModel::onAction, onNavigateToForecast = onNavigateToForecast)
        }

        PermissionGateState.DeniedWithRationale -> {
            RationaleScreen(
                onGrant = {
                    if (activity?.shouldShowLocationRationale() == true){
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )

                    }
                },
                onDismiss = {
                    permissionState = PermissionGateState.DeniedPermanently
                }
            )
        }

        PermissionGateState.DeniedPermanently -> {
            PermissionDeniedScreen(
                onOpenSettings = { activity?.openAppSettings() },
                onDismiss = { /* stay here until granted */ }
            )
        }

        PermissionGateState.Default -> {

        }
    }
}

private fun Activity.shouldShowLocationRationale(): Boolean =
    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ||
        shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)

private fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is android.content.ContextWrapper -> baseContext.findActivity()
    else -> null
}

private fun Activity.openAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", packageName, null)
        addCategory(Intent.CATEGORY_DEFAULT)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}