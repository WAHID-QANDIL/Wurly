package com.wahid.wurly.domain.usecase

import com.wahid.wurly.domain.model.weather.City
import com.wahid.wurly.domain.repository.WeatherRepository
import javax.inject.Inject

class RemoveCityFromFavorites @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(city: City) = repository.removeCityFromFavorites(city)
}