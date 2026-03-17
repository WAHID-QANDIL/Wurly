package com.wahid.wurly.data.remote.api.dto.model.forecast_dayweather

import com.wahid.wurly.data.common.model.Clouds
import com.wahid.wurly.data.common.model.Main
import com.wahid.wurly.data.common.model.Weather
import com.wahid.wurly.data.common.model.Wind
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastDay(
    val clouds: Clouds,
    @SerialName("dt")
    val dayTime: Long,
    @SerialName("dt_txt")
    val dayTimeString: String,
    val main: Main,
    val pop: Double,
    val sys: Sys,
    val visibility: Int,
    val weather: Weather,
    val wind: Wind
)