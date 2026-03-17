package com.wahid.wurly.data.local.database.entity

import androidx.room.Embedded
import androidx.room.Relation
data class ForecastDayWeather(
    @Embedded val city: City,
    @Relation(
        parentColumn = "id",
        entityColumn = "cityId"
    )
    val dayWeatherList: List<DayWeather>
)