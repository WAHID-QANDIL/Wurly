package com.wahid.wurly.domain.repository

import com.wahid.wurly.domain.model.weather.City
import com.wahid.wurly.domain.model.weather.DayWeather
import com.wahid.wurly.domain.model.weather.ForecastDays
import com.wahid.wurly.domain.model.weather.FavoriteCityWeather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository : DomainBaseRepository {
    suspend fun getCurrentDayWeather(filters: Map<String, String>): Flow<DayWeather>
    suspend fun getACityById(cityId: Long): ForecastDays

    suspend fun getLatestCachedForecast(): Flow<ForecastDays?>
    suspend fun getNextForecastDays(): Flow<ForecastDays?>
    suspend fun getForecastDaysWeather(
        filters: Map<String, String>,
        isFavorite: Boolean
    ): Flow<ForecastDays>

    suspend fun getFiveDaySummaries(
        filters: Map<String, String>,
        isFavorite: Boolean
    ): Flow<List<DayWeather>>

    fun getFavoriteCities(): Flow<List<City>>
    fun getFavoriteCityWeathers(): Flow<List<FavoriteCityWeather>>
    suspend fun addCityToFavorites(city: City)
    suspend fun removeCityFromFavorites(city: City)
}