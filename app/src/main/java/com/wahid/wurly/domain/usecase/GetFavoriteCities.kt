package com.wahid.wurly.domain.usecase

import com.wahid.wurly.domain.model.weather.City
import com.wahid.wurly.domain.repository.WeatherRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetFavoriteCities @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(): Flow<List<City>> = repository.getFavoriteCities()
}