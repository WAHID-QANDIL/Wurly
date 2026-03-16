package com.wahid.wurly.domain.repository

import com.wahid.wurly.domain.model.settings.UserSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository : DomainBaseRepository {
    fun getUserSettings(): Flow<UserSettings>
    suspend fun updateUserSettings(settings: UserSettings)
}