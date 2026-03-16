package com.wahid.wurly.domain.usecase

import com.wahid.wurly.domain.model.settings.UserSettings
import com.wahid.wurly.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateUserSettings @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(settings: UserSettings) {
        repository.updateUserSettings(settings)
    }
}