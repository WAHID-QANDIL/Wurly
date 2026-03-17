package com.wahid.wurly.data.common.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Wind(
    @SerialName("deg")
    val windDirectionDegrees: Int,
    @SerialName("gust")
    val windGust: Double?,
    @SerialName("speed")
    val windSpeed: Double
)