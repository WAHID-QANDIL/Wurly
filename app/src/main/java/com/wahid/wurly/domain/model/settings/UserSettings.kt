package com.wahid.wurly.domain.model.settings

import com.wahid.wurly.domain.model.weather.WeatherUnit
import com.wahid.wurly.domain.model.weather.WindUnit
import kotlinx.serialization.Serializable

@Serializable()
data class UserSettings(
    val weatherUnit: WeatherUnit = WeatherUnit.METRIC,
    val windUnit: WindUnit = WindUnit.fromWeatherUnit(weatherUnit),
    val language: String = SupportedLanguage.ENGLISH.code,
    val isNotificationsEnabled: Boolean = true,
    val isLocationEnabled: Boolean = false,
    val defaultLocation: LocationPref? = null,
    val isCached: Boolean = false
 ) {


    companion object {
         fun default(): UserSettings = UserSettings()
     }
 }

@Serializable
data class LocationPref(
    val latitude: Double,
    val longitude: Double,
)