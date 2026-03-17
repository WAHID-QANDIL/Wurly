package com.wahid.wurly.domain.usecase.alerts

import com.wahid.wurly.domain.model.weather.WeatherAlert
import com.wahid.wurly.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlerts @Inject constructor(
    private val repository: WeatherRepository,
) {
    operator fun invoke(): Flow<List<WeatherAlert>> = repository.observeAlerts()
}