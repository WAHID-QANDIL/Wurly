package com.wahid.wurly.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wahid.wurly.domain.model.settings.UserSettings
import com.wahid.wurly.domain.usecase.GetUserSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * Holds user settings as a hot StateFlow so changes propagate app-wide.
 */
@HiltViewModel
class AppSettingsViewModel @Inject constructor(
    private val getUserSettings: GetUserSettings,
) : ViewModel() {

    val settings: StateFlow<UserSettings> = getUserSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = UserSettings.default(),
        )
}