package com.wahid.wurly.di

import com.wahid.wurly.data.local.datasource.LocalWeatherDatasource
import com.wahid.wurly.data.local.datasource.LocalWeatherDatasourceImpl
import com.wahid.wurly.data.remote.datasource.RemoteWeatherDatasource
import com.wahid.wurly.data.remote.datasource.RemoteWeatherDatasourceImpl
import com.wahid.wurly.data.repository.SettingsRepositoryImpl
import com.wahid.wurly.data.repository.WeatherRepositoryImpl
import com.wahid.wurly.domain.repository.SettingsRepository
import com.wahid.wurly.domain.repository.WeatherRepository
import com.wahid.wurly.domain.usecase.AddCityToFavorites
import com.wahid.wurly.domain.usecase.GetCityForecastById
import com.wahid.wurly.domain.usecase.GetCurrentWeather
import com.wahid.wurly.domain.usecase.GetDayForecast
import com.wahid.wurly.domain.usecase.GetFavoriteCities
import com.wahid.wurly.domain.usecase.GetFavoriteCityWeathers
import com.wahid.wurly.domain.usecase.GetFiveDaySummaries
import com.wahid.wurly.domain.usecase.GetLatestCachedForecast
import com.wahid.wurly.domain.usecase.GetNextForecastDays
import com.wahid.wurly.domain.usecase.GetUserSettings
import com.wahid.wurly.domain.usecase.RemoveCityFromFavorites
import com.wahid.wurly.domain.usecase.SettingsUseCases
import com.wahid.wurly.domain.usecase.UpdateUserSettings
import com.wahid.wurly.domain.usecase.WeatherUseCases
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideLocalDatasource(localDatasourceImpl: LocalWeatherDatasourceImpl): LocalWeatherDatasource

    @Binds
    @Singleton
    abstract fun provideRemoteDatasource(remoteWeatherDatasourceImpl: RemoteWeatherDatasourceImpl): RemoteWeatherDatasource

    @Binds
    @Singleton
    abstract fun provideRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository

    @Binds
    @Singleton
    abstract fun provideSettingsRepository(settingsRepositoryImpl: SettingsRepositoryImpl): SettingsRepository

     companion object {
         @Provides
         @Singleton
         fun provideSettingsUseCases(settingsRepository: SettingsRepository): SettingsUseCases {
             return SettingsUseCases(
                 getUserSettings = GetUserSettings(settingsRepository),
                 updateUserSettings = UpdateUserSettings(settingsRepository)
             )
         }

         @Provides
         @Singleton
         fun provideWeatherUseCases(weatherRepository: WeatherRepository): WeatherUseCases {
             return WeatherUseCases(
                 getCurrentWeather = GetCurrentWeather(weatherRepository),
                 getDayForecast = GetDayForecast(weatherRepository),
                 getFiveDaySummaries = GetFiveDaySummaries(weatherRepository),
                 getLatestCachedForecast = GetLatestCachedForecast(weatherRepository),
                 getNextForecastDays = GetNextForecastDays(weatherRepository),
                 getCityForecastById = GetCityForecastById(weatherRepository),
                 getFavoriteCities = GetFavoriteCities(weatherRepository),
                 getFavoriteCityWeathers = GetFavoriteCityWeathers(weatherRepository),
                 addCityToFavorites = AddCityToFavorites(weatherRepository),
                 removeCityFromFavorites = RemoveCityFromFavorites(weatherRepository)
             )
         }
      }

}