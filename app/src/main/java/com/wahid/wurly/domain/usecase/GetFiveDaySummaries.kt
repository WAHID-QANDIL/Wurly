package com.wahid.wurly.domain.usecase

import com.wahid.wurly.domain.model.weather.DayWeather
import com.wahid.wurly.domain.repository.WeatherRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetFiveDaySummaries @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(filters: Map<String, String>, isFavorite: Boolean): Flow<List<DayWeather>> {
        return repository.getFiveDaySummaries(filters = filters, isFavorite = isFavorite)
    }
}