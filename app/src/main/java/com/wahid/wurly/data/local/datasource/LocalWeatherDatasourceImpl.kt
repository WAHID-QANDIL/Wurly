package com.wahid.wurly.data.local.datasource

import com.wahid.wurly.data.local.database.WeatherDatabase
import com.wahid.wurly.data.local.database.dao.WeatherDao
import com.wahid.wurly.data.local.database.entity.City
import com.wahid.wurly.data.local.database.entity.DayWeather
import com.wahid.wurly.data.local.database.entity.ForecastDayWeather
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalWeatherDatasourceImpl @Inject constructor(
    database: WeatherDatabase
) : LocalWeatherDatasource {
    private val weatherDao: WeatherDao = database.getWeatherDao()

    override suspend fun upsertCity(city: City) {
        weatherDao.upsertCity(city = city)
    }

    override suspend fun upsertDayWeather(dayWeather: List<DayWeather>) {
        weatherDao.upsertDayWeather(dayWeather = dayWeather)
    }

    override suspend fun getForecastByCityId(cityId: Long): ForecastDayWeather? {
        return weatherDao.getForecastByCityId(cityId = cityId)
    }

    override fun observeForecastByCityId(cityId: Long): Flow<ForecastDayWeather?> {
        return weatherDao.observeForecastByCityId(cityId = cityId)
    }

    override fun observeLatestForecast(): Flow<ForecastDayWeather?> {
        return weatherDao.observeLatestForecast()
    }

    override fun observeFavoriteForecasts(): Flow<List<ForecastDayWeather>> {
        return weatherDao.observeFavoriteForecasts()
    }

    override fun getFavoriteCities(): Flow<List<City>> {
        return weatherDao.getFavoriteCities()
    }

    override suspend fun addCityToFavorites(city: City) {
        weatherDao.addCityToFavorites(city = city)
    }

    override suspend fun removeCityFromFavorites(city: City) {
        weatherDao.removeCityFromFavorites(city = city)
    }

}