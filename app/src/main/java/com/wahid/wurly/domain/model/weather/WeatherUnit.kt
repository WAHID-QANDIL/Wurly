package com.wahid.wurly.domain.model.weather

import kotlinx.serialization.Serializable

/**
 * Supported measurement systems for weather data and API queries.
 */
@Serializable
enum class WeatherUnit(val unit: String) {
    METRIC("metric"),
    IMPERIAL("imperial"),
}
@Serializable
enum class WindUnit(val unit: String) {

        METER_PER_SECOND("m/s"),
        MILE_PER_HOUR("mph");

        companion object {
            fun fromWeatherUnit(weatherUnit: WeatherUnit): WindUnit {
                return when (weatherUnit) {
                        WeatherUnit.METRIC -> METER_PER_SECOND
                        WeatherUnit.IMPERIAL -> MILE_PER_HOUR
                }
            }
        }
}