package com.wahid.wurly.domain.usecase

import com.wahid.wurly.domain.model.weather.FavoriteCityWeather
import com.wahid.wurly.domain.repository.WeatherRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetFavoriteCityWeathers @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(): Flow<List<FavoriteCityWeather>> = repository.getFavoriteCityWeathers()
}