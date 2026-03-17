package com.wahid.wurly.data.remote.datasource

import com.wahid.wurly.data.remote.api.WeatherApiService
import com.wahid.wurly.data.remote.api.dto.DayWeather
import com.wahid.wurly.data.remote.api.dto.ForecastDayWeather
import com.wahid.wurly.data.remote.api.safeApiCall
import com.wahid.wurly.utils.ApiResult
import javax.inject.Inject

class RemoteWeatherDatasourceImpl @Inject constructor(
    val weatherApiService: WeatherApiService
) : RemoteWeatherDatasource {
    override suspend fun getCurrentWeather(weatherInfo: Map<String, String>): ApiResult<DayWeather> {

        return weatherApiService.safeApiCall {
            weatherApiService.getCurrentWeather(weatherInfo = weatherInfo)
        }

    }

    override suspend fun getForecastDays(weatherInfo: Map<String, String>): ApiResult<ForecastDayWeather> {
        return weatherApiService.safeApiCall {
            weatherApiService.getForecastDays(weatherInfo = weatherInfo)
        }

    }
}