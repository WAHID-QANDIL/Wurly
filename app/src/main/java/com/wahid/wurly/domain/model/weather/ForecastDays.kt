package com.wahid.wurly.domain.model.weather

data class ForecastDays(
    val city: City,
    val list: List<DayWeather>,
)