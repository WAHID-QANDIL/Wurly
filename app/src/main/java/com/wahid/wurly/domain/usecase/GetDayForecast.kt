package com.wahid.wurly.domain.usecase

import com.wahid.wurly.domain.model.weather.ForecastDays
import com.wahid.wurly.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDayForecast @Inject constructor (
    val repository: WeatherRepository
) {

    suspend operator fun invoke(filters: Map<String, String> ,isFavorite: Boolean): Flow<ForecastDays> {
        return repository.getForecastDaysWeather(filters = filters, isFavorite = isFavorite)
    }
}