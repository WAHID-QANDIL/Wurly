package com.wahid.wurly.data.repository

import android.util.Log
import app.cash.turbine.test
import com.wahid.wurly.data.common.model.Coordinates
import com.wahid.wurly.data.local.database.entity.City as CityEntity
import com.wahid.wurly.data.local.database.entity.ForecastDayWeather as ForecastEntity
import com.wahid.wurly.data.local.datasource.LocalWeatherDatasource
import com.wahid.wurly.data.local.datastore.LocalDataStore
import com.wahid.wurly.data.remote.api.dto.ForecastDayWeather as ForecastDto
import com.wahid.wurly.data.remote.api.dto.model.forecast_dayweather.City as CityDto
import com.wahid.wurly.data.remote.datasource.RemoteWeatherDatasource
import com.wahid.wurly.domain.model.settings.UserSettings
import com.wahid.wurly.utils.ApiResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WeatherRepositoryImplTest {

    private lateinit var repository: WeatherRepositoryImpl
    private val localDatasource: LocalWeatherDatasource = mockk(relaxed = true)
    private val remoteDatasource: RemoteWeatherDatasource = mockk()
    private val localDataStore: LocalDataStore = mockk(relaxed = true)

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.d(any<String>(), any<String>()) } returns 0
        every { Log.e(any<String>(), any<String>()) } returns 0
        every { Log.w(any<String>(), any<String>()) } returns 0
        every { Log.i(any<String>(), any<String>()) } returns 0

        repository = WeatherRepositoryImpl(
            localDatasource = localDatasource,
            remoteWeatherDatasource = remoteDatasource,
            localDataStore = localDataStore
        )
    }

    @Test
    fun `getForecastDaysWeather should fetch from remote and cache when forceRefresh is true`() = runTest {
        val filters = mapOf("lat" to "0.0", "lon" to "0.0")
        val userSettings = UserSettings(isCached = false)

        val cityDto = CityDto(
            id = 1L,
            name = "Mock City",
            coordinates = Coordinates(0.0, 0.0),
            country = "Country",
            population = 0,
            sunrise = 0,
            sunset = 0,
            timezone = 0
        )
        val forecastDto = ForecastDto(
            city = cityDto,
            numberOfDays = 0,
            responseCode = "200",
            list = emptyList(),
            message = 0
        )

        coEvery { localDataStore.userSettings } returns flowOf(userSettings)
        every { localDatasource.getAllCities() } returns flowOf(emptyList())
        coEvery { remoteDatasource.getForecastDays(any()) } returns ApiResult.Success(forecastDto)

        val dummyEntity = ForecastEntity(
            city = CityEntity(
                id = 1L,
                isFavorite = false,
                coordinates = Coordinates(0.0, 0.0),
                country = "Country",
                name = "Mock City",
                population = 0,
                sunrise = 0,
                sunset = 0,
                timezone = 0
            ),
            dayWeatherList = emptyList()
        )
        coEvery { localDatasource.observeForecastByCityId(any()) } returns flowOf(dummyEntity)

        repository.getForecastDaysWeather(filters, isFavorite = false, forceRefresh = true).test {
            val result = awaitItem()
            assertEquals("Mock City", result.city.name)
            awaitComplete()
        }

        coVerify { remoteDatasource.getForecastDays(filters) }
        coVerify { localDatasource.upsertCity(any()) }
        coVerify { localDatasource.upsertDayWeather(any()) }
    }

    @Test
    fun `getForecastDaysWeather should use closest cached city when request coordinates are nearby`() = runTest {
        val filters = mapOf("lat" to "30.0001", "lon" to "31.0001")
        val userSettings = UserSettings(isCached = true)

        val cachedCity = CityEntity(
            id = 42L,
            isFavorite = false,
            coordinates = Coordinates(30.0, 31.0),
            country = "Country",
            name = "Nearby City",
            population = 0,
            sunrise = 0,
            sunset = 0,
            timezone = 0
        )
        val cachedForecast = ForecastEntity(
            city = cachedCity,
            dayWeatherList = emptyList()
        )

        coEvery { localDataStore.userSettings } returns flowOf(userSettings)
        every { localDatasource.getAllCities() } returns flowOf(listOf(cachedCity))
        every { localDatasource.observeForecastByCityId(42L) } returns flowOf(cachedForecast)

        repository.getForecastDaysWeather(filters, isFavorite = false, forceRefresh = false).test {
            val result = awaitItem()
            assertEquals(42L, result.city.id)
            awaitComplete()
        }

        coVerify(exactly = 0) { remoteDatasource.getForecastDays(any()) }
    }
}