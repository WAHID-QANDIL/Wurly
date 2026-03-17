package com.wahid.wurly.domain.usecase

import com.wahid.wurly.domain.model.weather.ForecastDays
import com.wahid.wurly.domain.repository.WeatherRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetNextForecastDays @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(): Flow<ForecastDays?> = repository.getNextForecastDays()
}