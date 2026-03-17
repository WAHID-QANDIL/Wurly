package com.wahid.wurly.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.wahid.wurly.data.local.datastore.LocalDataStore.Companion.DATA_STORE_NAME
import com.wahid.wurly.data.local.datastore.SettingsPrefsSerializer
import com.wahid.wurly.domain.model.settings.UserSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {


    @Singleton
    @Provides
    fun provideLocalDataStore(
        @ApplicationContext context: Context,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        @ApplicationScope appScope: CoroutineScope
    ): DataStore<UserSettings> = DataStoreFactory.create(
        serializer = SettingsPrefsSerializer(defaultValue = UserSettings.default()),
        scope = CoroutineScope(appScope.coroutineContext + ioDispatcher),
        // note why we use appScope here, because we want to use the same scope for the whole app, and we want to use the same dispatcher for the whole app, so we combine them here
        produceFile = { context.dataStoreFile(DATA_STORE_NAME) }
    )
}