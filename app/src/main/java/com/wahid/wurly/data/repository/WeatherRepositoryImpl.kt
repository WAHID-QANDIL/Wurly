package com.wahid.wurly.data.repository

import android.util.Log
import com.wahid.wurly.data.common.mapper.WeatherDayMappers.toDomain
import com.wahid.wurly.data.common.mapper.WeatherDayMappers.toDomainCity
import com.wahid.wurly.data.common.mapper.WeatherDayMappers.toDomainDayWeather
import com.wahid.wurly.data.local.database.entity.City as CityEntity
import com.wahid.wurly.data.local.database.entity.DayWeather as DayWeatherEntity
import com.wahid.wurly.data.remote.api.dto.model.forecast_dayweather.ForecastDay
import com.wahid.wurly.data.remote.api.dto.ForecastDayWeather as ForecastDto
import com.wahid.wurly.data.local.database.entity.ForecastDayWeather as ForecastEntity
import com.wahid.wurly.data.local.datasource.LocalWeatherDatasource
import com.wahid.wurly.data.local.datastore.LocalDataStore
import com.wahid.wurly.data.remote.datasource.RemoteWeatherDatasource
import com.wahid.wurly.domain.exception.toCustomRemoteExceptionDomainModel
import com.wahid.wurly.domain.model.weather.City
import com.wahid.wurly.domain.model.weather.DayWeather
import com.wahid.wurly.domain.model.weather.ForecastDays
import com.wahid.wurly.domain.model.weather.FavoriteCityWeather
import com.wahid.wurly.domain.repository.WeatherRepository
import com.wahid.wurly.utils.ApiResult
import java.time.Instant
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class WeatherRepositoryImpl @Inject constructor(
    private val localDatasource: LocalWeatherDatasource,
    private val remoteWeatherDatasource: RemoteWeatherDatasource,
    private val localDataStore: LocalDataStore
) : WeatherRepository {
    companion object {
        private const val TAG = "WeatherRepositoryImpl"
    }


    override suspend fun getCurrentDayWeather(filters: Map<String, String>): Flow<DayWeather> =
        flow {
            val dayWeather = remoteWeatherDatasource
                .getCurrentWeather(weatherInfo = filters)
                .getOrThrow()
            emit(dayWeather.toDomainDayWeather())
        }.catch { throw it.toCustomRemoteExceptionDomainModel() }

    override suspend fun getForecastDaysWeather(
        filters: Map<String, String>,
        isFavorite: Boolean
    ): Flow<ForecastDays> =
        flow {
            val settings = localDataStore.userSettings.first()
            val cached = localDatasource.observeLatestForecast().firstOrNull()
            val shouldRefresh = isFavorite || !settings.isCached || cached == null

            val cityId = if (shouldRefresh) {
                val forecastDto = remoteWeatherDatasource
                    .getForecastDays(weatherInfo = filters)
                    .getOrThrow()
                val id = cacheForecastResponse(forecastDto, isFavorite = isFavorite)
                localDataStore.setSettings(settings.copy(isCached = true))
                id
            } else {
                cached.city.id
            }

            emitAll(
                localDatasource
                    .observeForecastByCityId(cityId = cityId)
                    .mapWithWindow(requireMessage = { "No cached forecast for cityId=$cityId" })
            )
        }.catch { throw it.toCustomRemoteExceptionDomainModel() }

    override suspend fun getACityById(cityId: Long): ForecastDays {
        val forecast = localDatasource.getForecastByCityId(cityId = cityId)
        return requireNotNull(forecast) { "No cached forecast for cityId=$cityId" }
            .withinFiveDayWindow()
            .toDomain()
    }

    override suspend fun getFiveDaySummaries(
        filters: Map<String, String>,
        isFavorite: Boolean
    ): Flow<List<DayWeather>> {
        // reuse forecast fetch/cache path, then derive 5-day buckets for list UI
        return getForecastDaysWeather(filters, isFavorite).map { it.toFiveDaySummaries().list }
    }

    override fun getFavoriteCities(): Flow<List<City>> {
        return localDatasource.getFavoriteCities()
            .map {
                it.map { city -> city.toDomainCity() }
            }
    }

    override fun getFavoriteCityWeathers(): Flow<List<FavoriteCityWeather>> {
         return localDatasource.observeFavoriteForecasts()
             .map { forecasts ->
                 forecasts.mapNotNull { forecast ->
                     val first = forecast.dayWeatherList.minByOrNull { it.dayTime } ?: return@mapNotNull null
                     FavoriteCityWeather(
                         city = forecast.city.toDomainCity(),
                         temperature = first.main.temp,
                         condition = first.weather.description,
                         icon = first.weather.icon,
                         dayTime = first.dayTime,
                     )
                 }
             }
     }

    override suspend fun addCityToFavorites(city: City) {
        localDatasource.addCityToFavorites(city = city.toEntity(isFavorite = true))
    }

    override suspend fun removeCityFromFavorites(city: City) {
        localDatasource.removeCityFromFavorites(city = city.toEntity(isFavorite = true))
    }

    override suspend fun getLatestCachedForecast(): Flow<ForecastDays> =
        flow {
            emitAll(
                localDatasource
                    .observeLatestForecast()
                    .mapWithWindow(requireMessage = { "No cached forecast" })
            )
        }

    override suspend fun getNextForecastDays(): Flow<ForecastDays?> {
        return flow {
            val nextForecast = localDatasource.observeLatestForecast()
                .map { forecast ->
                    requireNotNull(forecast) { "No cached forecast" }
                    forecast.toDomain().toFiveDaySummaries()
                }
            emitAll(nextForecast)
        }
    }


    private suspend fun cacheForecastResponse(forecastDto: ForecastDto, isFavorite: Boolean): Long {
        val cityEntity = forecastDto.toCityEntity(isFavorite)
        Log.d(TAG, "cacheForecastResponse: $cityEntity, $forecastDto")
        val dayWeatherEntities = forecastDto.list.toDayWeatherEntities(cityEntity.id)
        localDatasource.upsertCity(city = cityEntity)
        localDatasource.upsertDayWeather(dayWeather = dayWeatherEntities)
        return cityEntity.id
    }
}

private fun Flow<ForecastEntity?>.mapWithWindow(requireMessage: () -> String): Flow<ForecastDays> =
    map { forecast ->
        requireNotNull(forecast, requireMessage)
            .withinFiveDayWindow()
            .toDomain()
    }

private fun ForecastDto.toCityEntity(isFavorite: Boolean): CityEntity = CityEntity(
    id = city.id,
    coordinates = city.coordinates,
    country = city.country,
    name = city.name,
    population = city.population,
    sunrise = city.sunrise,
    sunset = city.sunset,
    timezone = city.timezone,
    isFavorite = isFavorite
)

private fun List<ForecastDay>.toDayWeatherEntities(cityId: Long): List<DayWeatherEntity> =
    map { forecastDay ->
        DayWeatherEntity(
            dayTime = forecastDay.dayTime,
            cityId = cityId,
            clouds = forecastDay.clouds,
            dayTimeString = forecastDay.dayTimeString,
            main = forecastDay.main,
            pop = forecastDay.pop,
            sys = forecastDay.sys,
            visibility = forecastDay.visibility,
            weather = forecastDay.weather,
            wind = forecastDay.wind,
        )
    }

private fun <T> ApiResult<T>.getOrThrow(): T = when (this) {
    is ApiResult.Success -> data
    is ApiResult.HttpError -> throw IllegalStateException("Http ${this.code}: ${this.message ?: "Unknown"}")
    is ApiResult.NetworkError -> throw exception
    is ApiResult.UnknownError -> throw throwable
}

private fun ForecastEntity.withinFiveDayWindow(): ForecastEntity {
    val nowSeconds = System.currentTimeMillis() / 1000
    val horizonSeconds = nowSeconds + TimeUnit.DAYS.toSeconds(5)
    val filtered = dayWeatherList
        .filter { it.dayTime in nowSeconds..horizonSeconds }
        .sortedBy { it.dayTime }
    return copy(dayWeatherList = filtered)
}

private fun ForecastDays.toFiveDaySummaries(): ForecastDays{
    val fiveDaySummaries = list
        .groupBy { forecast ->
            Instant.ofEpochSecond(forecast.dayTime).atZone(ZoneOffset.UTC).toLocalDate()
        }
        .values
        .mapNotNull { dayEntries -> dayEntries.minByOrNull { it.dayTime } }
        .sortedBy { it.dayTime }
    return copy(list = fiveDaySummaries)
}