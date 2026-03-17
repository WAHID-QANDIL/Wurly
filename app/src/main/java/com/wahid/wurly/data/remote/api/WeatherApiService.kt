package com.wahid.wurly.data.remote.api

import com.wahid.wurly.data.remote.api.dto.DayWeather
import com.wahid.wurly.data.remote.api.dto.ForecastDayWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface WeatherApiService : ApiService{
    @GET("weather")
    suspend fun getCurrentWeather(
        @QueryMap(encoded = true) weatherInfo: Map<String,String>
    ): Response<DayWeather>

    @GET("forecast")
    suspend fun getForecastDays(
        @QueryMap(encoded = true) weatherInfo: Map<String,String>
    ): Response<ForecastDayWeather>

}