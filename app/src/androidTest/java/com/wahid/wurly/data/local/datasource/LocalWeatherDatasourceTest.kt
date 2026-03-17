package com.wahid.wurly.data.local.datasource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.wahid.wurly.data.common.model.Coordinates
import com.wahid.wurly.data.local.database.WeatherDatabase
import com.wahid.wurly.data.local.database.entity.City
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalWeatherDatasourceTest {

    private lateinit var database: WeatherDatabase
    private lateinit var datasource: LocalWeatherDatasource

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).allowMainThreadQueries().build()
        datasource = LocalWeatherDatasourceImpl(database)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun datasource_upsert_and_observe() = runTest {
        val city = City(1L, false, Coordinates(0.0, 0.0), "Country", "Name", 0, 0, 0, 0)
        datasource.upsertCity(city)

        val result = datasource.observeLatestForecast().first()
        assertEquals("Name", result?.city?.name)
    }
}