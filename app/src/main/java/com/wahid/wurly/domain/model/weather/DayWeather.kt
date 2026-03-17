package com.wahid.wurly.domain.model.weather

import com.wahid.wurly.data.remote.api.dto.model.dayweather.Sys

data class DayWeather(
    val dayTime: Long,
    val cityName: String,
    val condition: Weather,
    val cityId: Long,
    val all: Int,
    val dayTimeString: String,
    val feelsLike: Double,
    val grandLevel: Double,
    val humidity: Double,
    val pressure: Double,
    val seaLevel: Double,
    val temp: Double,
    val tempKf: Double?,
    val tempMax: Double,
    val tempMin: Double,
    val visibility: Double,
    val windDirectionDegrees: Int,
    val windGust: Double,
    val windSpeed: Double,
    val sys: Sys
)
data class Weather(
    val description: String,
    val icon: String,
    val id: Long,
    val main: String
)