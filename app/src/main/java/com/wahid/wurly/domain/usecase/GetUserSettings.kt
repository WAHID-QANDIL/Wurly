package com.wahid.wurly.domain.usecase

import com.wahid.wurly.domain.model.settings.UserSettings
import com.wahid.wurly.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserSettings @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<UserSettings> = repository.getUserSettings()
}