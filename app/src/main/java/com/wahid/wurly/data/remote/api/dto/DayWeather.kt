package com.wahid.wurly.data.remote.api.dto

import com.wahid.wurly.data.common.model.Clouds
import com.wahid.wurly.data.common.model.Coordinates
import com.wahid.wurly.data.common.model.Main
import com.wahid.wurly.data.remote.api.dto.model.dayweather.Sys
import com.wahid.wurly.data.common.model.Weather
import com.wahid.wurly.data.common.model.Wind
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DayWeather(
    @SerialName("coord")
    val coordinates: Coordinates,
    @SerialName("weather")
    val weather: Weather,
    @SerialName("base")
    val base: String,
    @SerialName("main")
    val main: Main,
    @SerialName("visibility")
    val visibility: Int,
    @SerialName("wind")
    val wind: Wind,
    @SerialName("clouds")
    val clouds: Clouds,
    @SerialName("dt")
    val dayTime: Long,
    @SerialName("sys")
    val sys: Sys,
    @SerialName("timezone")
    val timezone: Long,
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val cityName: String,
    @SerialName("cod")
    val responseCode: Int
)