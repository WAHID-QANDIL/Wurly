package com.wahid.wurly.domain.usecase.alerts

import com.wahid.wurly.domain.model.weather.WeatherAlert
import com.wahid.wurly.domain.repository.WeatherRepository
import javax.inject.Inject

class UpsertAlert @Inject constructor(
    private val repository: WeatherRepository,
) {
    suspend operator fun invoke(alert: WeatherAlert) = repository.upsertAlert(alert)
}