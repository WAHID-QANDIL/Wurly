package com.wahid.wurly.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_alert")
data class WeatherAlertEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val alertCase: String,
    val style: String,
    val durationMillis: Long,
    val createdAt: Long,
    val enabled: Boolean = true,
    val targetTemperature: Int? = null,
)