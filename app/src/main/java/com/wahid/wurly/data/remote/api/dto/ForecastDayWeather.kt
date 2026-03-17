package com.wahid.wurly.data.remote.api.dto

import com.wahid.wurly.data.remote.api.dto.model.forecast_dayweather.City
import com.wahid.wurly.data.remote.api.dto.model.forecast_dayweather.ForecastDay
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastDayWeather(
    @SerialName("city")
    val city: City,
    @SerialName("cnt")
    val numberOfDays: Int,
    @SerialName("cod")
    val responseCode: String,
    @SerialName("list")
    val list: List<ForecastDay>,
    @SerialName("message")
    val message: Int
)