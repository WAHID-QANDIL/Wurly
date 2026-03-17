package com.wahid.wurly.data.local.database.entity

import androidx.room.Entity
import com.wahid.wurly.data.common.model.Coordinates

@Entity(tableName = "city", primaryKeys = ["id"])
data class City(
    val id: Long,
    val isFavorite: Boolean,
    val coordinates: Coordinates,
    val country: String,
    val name: String,
    val population: Long,
    val sunrise: Long,
    val sunset: Long,
    val timezone: Long
)