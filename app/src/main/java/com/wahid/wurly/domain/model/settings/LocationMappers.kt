package com.wahid.wurly.domain.model.settings

import com.mapbox.common.location.Location

/**
 * Helpers to convert between the serializable [LocationPref] DTO and Mapbox [Location].
 */
fun Location.toPref(): LocationPref = LocationPref(
    latitude = latitude,
    longitude = longitude,
)

fun LocationPref.toLocation(timestampMillis: Long = System.currentTimeMillis()): Location =
    Location.Builder()
        .latitude(latitude)
        .longitude(longitude)
        .timestamp(timestampMillis)
        .build()