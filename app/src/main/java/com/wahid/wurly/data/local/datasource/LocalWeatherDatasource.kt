package com.wahid.wurly.data.local.datasource

import com.wahid.wurly.data.local.database.entity.City
import com.wahid.wurly.data.local.database.entity.DayWeather
import com.wahid.wurly.data.local.database.entity.ForecastDayWeather
import kotlinx.coroutines.flow.Flow

interface LocalWeatherDatasource {

    suspend fun upsertCity(city: City)
    suspend fun upsertDayWeather(dayWeather: List<DayWeather>)
    suspend fun getForecastByCityId(cityId: Long): ForecastDayWeather?
    fun observeForecastByCityId(cityId: Long): Flow<ForecastDayWeather?>
    fun observeLatestForecast(): Flow<ForecastDayWeather?>
    fun observeFavoriteForecasts(): Flow<List<ForecastDayWeather>>

    fun getFavoriteCities(): Flow<List<City>>
    suspend fun addCityToFavorites(city: City)
    suspend fun removeCityFromFavorites(city: City)

    suspend fun upsertAlert(alert: com.wahid.wurly.data.local.database.entity.WeatherAlertEntity)
    suspend fun deleteAlert(id: Long)
    fun observeAlerts(): Flow<List<com.wahid.wurly.data.local.database.entity.WeatherAlertEntity>>

}