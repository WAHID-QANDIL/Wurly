package com.wahid.wurly.domain.model.weather

import com.wahid.wurly.presentation.screen.alerts.AlertStyle
import com.wahid.wurly.presentation.screen.alerts.AlertCase

data class WeatherAlert(
    val id: Long = 0,
    val alertCase: AlertCase,
    val style: AlertStyle,
    val durationMillis: Long,
    val createdAt: Long,
    val enabled: Boolean = true,
    val targetTemperature: Int? = null,
)