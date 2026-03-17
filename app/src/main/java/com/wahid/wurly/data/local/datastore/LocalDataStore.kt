package com.wahid.wurly.data.local.datastore

import androidx.datastore.core.DataStore
import com.wahid.wurly.domain.model.settings.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataStore @Inject constructor(
    private val dataStore: DataStore<UserSettings>
) {
    companion object{
         const val DATA_STORE_NAME = "user_settings.pb"
    }
    val userSettings: Flow<UserSettings> = dataStore.data
    val isCachedDataCached = userSettings.map { it.isCached }

    suspend fun setSettings(settings: UserSettings) {
        dataStore.updateData { settings }
    }
}