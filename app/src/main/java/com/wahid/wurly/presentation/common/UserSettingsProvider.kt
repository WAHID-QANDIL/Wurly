package com.wahid.wurly.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.wahid.wurly.domain.model.settings.UserSettings

val LocalUserSettings = staticCompositionLocalOf { UserSettings.default() }

@Composable
fun ProvideUserSettings(
    userSettings: UserSettings,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalUserSettings provides userSettings) {
        content()
    }
}