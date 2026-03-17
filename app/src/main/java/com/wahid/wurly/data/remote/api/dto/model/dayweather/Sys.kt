package com.wahid.wurly.data.remote.api.dto.model.dayweather

import kotlinx.serialization.Serializable

@Serializable

data class Sys(
    val country: String,
    val sunrise: Long,
    val sunset: Long,
)