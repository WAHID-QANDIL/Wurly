package com.wahid.wurly.domain.model.weather
data class City(
    val id: Long,
    val latitude: Double,
    val longitude: Double,
    val country: String,
    val name: String,
    val population: Long,
    val sunrise: Long,
    val sunset: Long,
    val timezone: Long
){
    fun toEntity(isFavorite: Boolean) = com.wahid.wurly.data.local.database.entity.City(
        id = id,
        isFavorite = isFavorite,
        coordinates = com.wahid.wurly.data.common.model.Coordinates(
            latitude = latitude,
            longitude = longitude
        ),
        country = country,
        name = name,
        population = population,
        sunrise = sunrise,
        sunset = sunset,
        timezone = timezone
    )
}