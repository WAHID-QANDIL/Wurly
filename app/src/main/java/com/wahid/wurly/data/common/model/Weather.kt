package com.wahid.wurly.data.common.model

import com.wahid.wurly.data.remote.api.WeatherKSerializer
import com.wahid.wurly.domain.model.weather.Weather
import kotlinx.serialization.Serializable

@Serializable(with = WeatherKSerializer::class)
data class Weather(
    val description: String,
    val main: String,
    val icon: String,
    val id: Long
){
    fun toDomain() =  Weather(
        description = description.uppercase(),
        icon = icon,
        id = id,
        main = main
    )
}