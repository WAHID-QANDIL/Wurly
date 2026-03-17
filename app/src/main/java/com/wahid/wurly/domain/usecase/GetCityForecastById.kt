package com.wahid.wurly.domain.usecase

import com.wahid.wurly.domain.model.weather.ForecastDays
import com.wahid.wurly.domain.repository.WeatherRepository
import javax.inject.Inject

class GetCityForecastById @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(cityId: Long): ForecastDays = repository.getACityById(cityId)
}