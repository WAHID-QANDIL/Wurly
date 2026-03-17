package com.wahid.wurly.data.remote.datasource

import com.wahid.wurly.data.remote.api.dto.DayWeather
import com.wahid.wurly.data.remote.api.dto.ForecastDayWeather
import com.wahid.wurly.utils.ApiResult

interface RemoteWeatherDatasource {

    suspend fun getCurrentWeather(
        weatherInfo: Map<String,String>
    ): ApiResult<DayWeather>


    suspend fun getForecastDays(
        weatherInfo: Map<String,String>
    ): ApiResult<ForecastDayWeather>
}