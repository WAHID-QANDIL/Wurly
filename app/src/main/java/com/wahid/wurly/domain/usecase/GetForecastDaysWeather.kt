package com.wahid.wurly.domain.usecase

import com.wahid.wurly.domain.repository.WeatherRepository
import javax.inject.Inject

class GetForecastDaysWeather @Inject constructor(
    private val repository: WeatherRepository
) {

    suspend operator fun invoke(filters: Map<String, String>, isFavorite: Boolean) =
        repository.getForecastDaysWeather(filters = filters, isFavorite = isFavorite)

}