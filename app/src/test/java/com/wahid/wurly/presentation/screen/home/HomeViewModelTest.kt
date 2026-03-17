package com.wahid.wurly.presentation.screen.home

import com.mapbox.common.location.Location
import app.cash.turbine.test
import com.wahid.wurly.data.remote.api.dto.model.dayweather.Sys
import com.wahid.wurly.domain.model.settings.UserSettings
import com.wahid.wurly.domain.model.weather.City
import com.wahid.wurly.domain.model.weather.DayWeather
import com.wahid.wurly.domain.model.weather.ForecastDays
import com.wahid.wurly.domain.model.weather.Weather
import com.wahid.wurly.domain.usecase.GetCurrentWeather
import com.wahid.wurly.domain.usecase.GetDayForecast
import com.wahid.wurly.domain.usecase.SettingsUseCases
import com.wahid.wurly.presentation.framwork.location.LocationServiceProvider
import com.wahid.wurly.utils.MainDispatcherRule
import com.wahid.wurly.utils.ResourceAccessor
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: HomeViewModel
    private val resourceAccessor: ResourceAccessor = mockk(relaxed = true)
    private val getDayForecast: GetDayForecast = mockk()
    private val getCurrentWeather: GetCurrentWeather = mockk()
    private val settingsUseCases: SettingsUseCases = mockk()

    @Before
    fun setup() {
        mockkObject(LocationServiceProvider)


        coEvery { settingsUseCases.getUserSettings() } returns flowOf(UserSettings.default())


        every { resourceAccessor.getString(any(), *anyVararg()) } returns "MockedString"
        every { resourceAccessor.getString(any()) } returns "MockedString"


        coEvery { LocationServiceProvider.getLastLocation() } returns null
        coEvery { getCurrentWeather(any()) } returns flowOf(mockk(relaxed = true))
        coEvery { getDayForecast(any(), any()) } returns flowOf(mockk(relaxed = true))
    }

    private fun initViewModel() {
        viewModel = HomeViewModel(
            resourceAccessor = resourceAccessor,
            getDayForecast = getDayForecast,
            getCurrentWeather = getCurrentWeather,
            settingsUseCases = settingsUseCases
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `when location is null, state is Error`() = runTest {
        coEvery { LocationServiceProvider.getLastLocation() } returns null

        initViewModel()

        viewModel.uiState.test {

            var lastState: HomeUiState = awaitItem()
            while (lastState is HomeUiState.Loading) {
                lastState = awaitItem()
            }

            assertTrue("Expected Error state but got $lastState", lastState is HomeUiState.Error)
            assertEquals("Failed to get location", (lastState as HomeUiState.Error).message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when location is valid, fetch weather data successfully`() = runTest {
        val mockLocation = mockk<Location>()
        every { mockLocation.latitude } returns 40.7128
        every { mockLocation.longitude } returns -74.0060
        coEvery { LocationServiceProvider.getLastLocation() } returns mockLocation

        val dummyWeather = DayWeather(
            dayTime = 1672531200L,
            cityName = "New York",
            condition = Weather(description = "Clear", icon = "01d", id = 800L, main = "Clear"),
            cityId = 5128581L,
            all = 0,
            dayTimeString = "2023-01-01 00:00:00",
            feelsLike = 15.0,
            grandLevel = 1010.0,
            humidity = 50.0,
            pressure = 1012.0,
            seaLevel = 1012.0,
            temp = 15.0,
            tempKf = null,
            tempMax = 16.0,
            tempMin = 14.0,
            visibility = 10000.0,
            windDirectionDegrees = 200,
            windGust = 4.0,
            windSpeed = 3.5,
            sys = Sys(country = "US", sunrise = 1672488000L, sunset = 1672524000L)
        )

        val dummyForecast = ForecastDays(
            city = City(
                id = 5128581L,
                name = "New York",
                latitude = 40.7128,
                longitude = -74.0060,
                country = "US",
                population = 8000000,
                timezone = -18000,
                sunrise = 1672488000L,
                sunset = 1672524000L
            ),
            list = listOf(dummyWeather)
        )

        coEvery { getCurrentWeather(any()) } returns flowOf(dummyWeather)
        coEvery { getDayForecast(any(), any()) } returns flowOf(dummyForecast)

        initViewModel()

        viewModel.uiState.test {
            var lastState: HomeUiState = awaitItem()
            // Skip loading states
            while (lastState !is HomeUiState.Success && lastState !is HomeUiState.Error) {
                lastState = awaitItem()
            }

            assertTrue("Expected Success state but got $lastState", lastState is HomeUiState.Success)
            val state = lastState as HomeUiState.Success
            assertEquals("New York, US", state.locationName)
            assertEquals("15", state.temperature)
            cancelAndIgnoreRemainingEvents()
        }
    }
}