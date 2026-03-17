package com.wahid.wurly.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.wahid.wurly.data.local.database.entity.City
import com.wahid.wurly.data.local.database.entity.DayWeather
import com.wahid.wurly.data.local.database.entity.ForecastDayWeather
import com.wahid.wurly.data.local.database.entity.WeatherAlertEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Upsert
    suspend fun upsertCity(city: City)

    @Upsert
    suspend fun upsertDayWeather(dayWeather: List<DayWeather>)

    @Transaction
    @Query("SELECT * FROM city WHERE id = :cityId LIMIT 1")
    suspend fun getForecastByCityId(cityId: Long): ForecastDayWeather?

    @Transaction
    @Query("SELECT * FROM city WHERE id = :cityId LIMIT 1")
    fun observeForecastByCityId(cityId: Long): Flow<ForecastDayWeather?>

    @Transaction
    @Query("SELECT * FROM city ORDER BY id DESC LIMIT 1")
    fun observeLatestForecast(): Flow<ForecastDayWeather?>

    @Transaction
    @Query("SELECT * FROM city WHERE isFavorite = 1")
    fun getFavoriteCities(): Flow<List<City>>

    @Transaction
    @Query("SELECT * FROM city WHERE isFavorite = 1")
    fun observeFavoriteForecasts(): Flow<List<ForecastDayWeather>>

    @Upsert
    suspend fun addCityToFavorites(city: City)

    @Delete
    suspend fun removeCityFromFavorites(city: City)

    @Upsert
    suspend fun upsertAlert(alert: WeatherAlertEntity)

    @Query("DELETE FROM weather_alert WHERE id = :id")
    suspend fun deleteAlert(id: Long)

    @Query("SELECT * FROM weather_alert ORDER BY createdAt DESC")
    fun observeAlerts(): Flow<List<WeatherAlertEntity>>
}