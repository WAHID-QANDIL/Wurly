package com.wahid.wurly.di

import com.wahid.wurly.domain.repository.WeatherRepository
import com.wahid.wurly.domain.usecase.alerts.DeleteAlert
import com.wahid.wurly.domain.usecase.alerts.GetAlerts
import com.wahid.wurly.domain.usecase.alerts.UpsertAlert
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Provides
    @Singleton
    fun provideGetAlerts(repository: WeatherRepository) = GetAlerts(repository)

    @Provides
    @Singleton
    fun provideUpsertAlert(repository: WeatherRepository) = UpsertAlert(repository)

    @Provides
    @Singleton
    fun provideDeleteAlert(repository: WeatherRepository) = DeleteAlert(repository)
}