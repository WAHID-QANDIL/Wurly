package com.wahid.wurly.data.repository

import com.wahid.wurly.data.local.datastore.LocalDataStore
import com.wahid.wurly.domain.model.settings.UserSettings
import com.wahid.wurly.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val localDataStore: LocalDataStore
) : SettingsRepository {
    override fun getUserSettings(): Flow<UserSettings>  = localDataStore.userSettings

    override suspend fun updateUserSettings(settings: UserSettings) {
        localDataStore.setSettings(settings)
    }
}