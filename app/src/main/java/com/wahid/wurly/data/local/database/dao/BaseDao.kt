package com.wahid.wurly.data.local.database.dao

import com.wahid.wurly.data.local.database.entity.City
import com.wahid.wurly.data.local.database.entity.DayWeather
import com.wahid.wurly.data.local.database.entity.ForecastDayWeather

/*
* A base dao for different local cache mechanisms*/

interface BaseDao {
    suspend fun upsertCity(city: City)
    suspend fun upsertDayWeather(dayWeather: List<DayWeather>)
    suspend fun getForecastByCityId(cityId: Long): ForecastDayWeather?
    fun observeForecastByCityId(cityId: Long): kotlinx.coroutines.flow.Flow<ForecastDayWeather?>
    fun observeLatestForecast(): kotlinx.coroutines.flow.Flow<ForecastDayWeather?>
}