package com.wahid.wurly.domain.usecase

/**
 * Aggregates weather-related use cases for convenient injection.
 */
data class WeatherUseCases(
    val getCurrentWeather: GetCurrentWeather,
    val getDayForecast: GetDayForecast,
    val getFiveDaySummaries: GetFiveDaySummaries,
    val getLatestCachedForecast: GetLatestCachedForecast,
    val getNextForecastDays: GetNextForecastDays,
    val getCityForecastById: GetCityForecastById,
    val getFavoriteCities: GetFavoriteCities,
    val getFavoriteCityWeathers: GetFavoriteCityWeathers,
    val addCityToFavorites: AddCityToFavorites,
    val removeCityFromFavorites: RemoveCityFromFavorites
)