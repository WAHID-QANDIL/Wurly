package com.wahid.wurly.data.remote.api.dto.model.forecast_dayweather

import com.wahid.wurly.data.common.model.Coordinates
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class City(
    @SerialName("coord")
    val coordinates: Coordinates,
    val country: String,
    val id: Long,
    val name: String,
    val population: Long,
    val sunrise: Long,
    val sunset: Long,
    val timezone: Long
)