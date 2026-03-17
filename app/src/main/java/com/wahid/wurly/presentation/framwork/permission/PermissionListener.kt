package com.wahid.wurly.presentation.framwork.permission

import com.mapbox.android.core.permissions.PermissionsListener


class PermissionsListener(
    val onExplanation: (permissionsToExplain: List<String>) -> Unit,
    val onGranted: () -> Unit,
    val onDenied: () -> Unit
) : PermissionsListener {
    override fun onExplanationNeeded(permissionsToExplain: List<String>) {
        onExplanation(permissionsToExplain)
    }
    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            onGranted()
        } else {
            onDenied()
        }
    }
}