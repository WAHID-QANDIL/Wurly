package com.wahid.wurly.domain.usecase

import com.wahid.wurly.domain.model.weather.DayWeather
import com.wahid.wurly.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentWeather @Inject constructor (
    private val repository: WeatherRepository
){
    suspend operator fun invoke(filters: Map<String, String>): Flow<DayWeather> {
        return repository.getCurrentDayWeather(filters = filters)
    }
}