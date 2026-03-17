package com.wahid.wurly.data.local.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.wahid.wurly.data.common.model.Coordinates
import com.wahid.wurly.data.local.database.WeatherDatabase
import com.wahid.wurly.data.local.database.entity.City
import com.wahid.wurly.data.local.database.entity.DayWeather
import com.wahid.wurly.data.common.model.Clouds
import com.wahid.wurly.data.common.model.Main
import com.wahid.wurly.data.common.model.Weather
import com.wahid.wurly.data.common.model.Wind
import com.wahid.wurly.data.remote.api.dto.model.forecast_dayweather.Sys
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherDaoTest {

    private lateinit var database: WeatherDatabase
    private lateinit var dao: WeatherDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.getWeatherDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun upsertCity_and_getForecastByCityId() = runTest {
        val city = City(
            id = 1L,
            isFavorite = true,
            coordinates = Coordinates(10.0, 20.0),
            country = "Country",
            name = "CityName",
            population = 1000,
            sunrise = 0,
            sunset = 0,
            timezone = 0
        )
        dao.upsertCity(city)

        val dayWeather = DayWeather(
            dayTime = 12345678L,
            cityId = 1L,
            clouds = Clouds(all = 10),
            dayTimeString = "2023-01-01",
            main = Main(feels_like = 20.0, humidity = 50.0, pressure = 1000.0, temp = 22.0, temp_max = 25.0, temp_min = 18.0, grnd_level = 1000.0, sea_level = 1000.0, temp_kf = null),
            pop = 0.5,
            sys = Sys(pod = "d"),
            visibility = 10000,
            weather = Weather(description = "Clear", icon = "01d", id = 800, main = "Clear"),
            wind = Wind(windDirectionDegrees = 180, windSpeed = 5.0, windGust = 10.0)
        )
        dao.upsertDayWeather(listOf(dayWeather))

        val result = dao.getForecastByCityId(1L)
        assertNotNull(result)
        assertEquals("CityName", result?.city?.name)
        assertEquals(1, result?.dayWeatherList?.size)
    }

    @Test
    fun observeLatestForecast_returnsMostRecentAddedCity() = runTest {
        val city1 = City(1L, false, Coordinates(0.0, 0.0), "C1", "City1", 0, 0, 0, 0)
        val city2 = City(2L, false, Coordinates(0.0, 0.0), "C2", "City2", 0, 0, 0, 0)

        dao.upsertCity(city1)
        dao.upsertCity(city2)

        val latest = dao.observeLatestForecast().first()
        assertNotNull(latest)
        assertEquals(2L, latest?.city?.id)
    }

    @Test
    fun getAllCities_returnsInsertedCities() = runTest {
        val city1 = City(1L, false, Coordinates(0.0, 0.0), "C1", "City1", 0, 0, 0, 0)
        val city2 = City(2L, false, Coordinates(1.0, 1.0), "C2", "City2", 0, 0, 0, 0)

        dao.upsertCity(city1)
        dao.upsertCity(city2)

        val all = dao.getAllCities().first()
        assertEquals(2, all.size)
    }
}