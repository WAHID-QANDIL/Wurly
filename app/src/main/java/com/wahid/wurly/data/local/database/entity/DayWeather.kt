package com.wahid.wurly.data.local.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import com.wahid.wurly.data.common.model.Clouds
import com.wahid.wurly.data.common.model.Main
import com.wahid.wurly.data.common.model.Weather
import com.wahid.wurly.data.common.model.Wind
import com.wahid.wurly.data.remote.api.dto.model.forecast_dayweather.Sys

@Entity(
    tableName = "day_weather_table",
    foreignKeys = [
        ForeignKey(
            entity = City::class,
            parentColumns = ["id"],
            childColumns = ["cityId"],
            onDelete = CASCADE,
        )],
    primaryKeys = ["dayTime", "cityId"],
    indices = [Index("cityId")],
)
data class DayWeather(
    val dayTime: Long,
    val cityId: Long,
    val clouds: Clouds,
    val dayTimeString: String,
    val main: Main,
    val pop: Double,
    val sys: Sys,
    val visibility: Int,
    val weather: Weather,
    val wind: Wind,
)