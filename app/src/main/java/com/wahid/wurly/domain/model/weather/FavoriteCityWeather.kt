package com.wahid.wurly.domain.model.weather

data class FavoriteCityWeather(
    val city: City,
    val temperature: Double,
    val condition: String,
    val icon: String,
    val dayTime: Long,
)