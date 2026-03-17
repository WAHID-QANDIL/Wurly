package com.wahid.wurly.data

import com.wahid.wurly.data.remote.api.dto.DayWeather
import com.wahid.wurly.data.remote.api.dto.ForecastDayWeather
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SerializationTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `forecast response decodes with int visibility`() {
        val dto = json.decodeFromString<ForecastDayWeather>(forecastPayload)
        assertEquals("200", dto.responseCode)
        assertTrue(dto.list.first().visibility is Int)
        assertEquals(10000, dto.list.first().visibility)
    }

    @Test
    fun `current response decodes with int visibility`() {
        val dto = json.decodeFromString<DayWeather>(currentPayload)
        assertEquals(10000, dto.visibility)
        assertEquals("EG", dto.sys.country)
        assertEquals(7922173L, dto.id)
    }
}

private const val forecastPayload = """
{
    "cod": "200",
    "message": 0,
    "cnt": 16,
    "list": [
        {
            "dt": 1773392400,
            "main": {
                "temp": 291.41,
                "feels_like": 290.73,
                "temp_min": 291.41,
                "temp_max": 295.08,
                "pressure": 1012,
                "sea_level": 1012,
                "grnd_level": 1005,
                "humidity": 55,
                "temp_kf": -3.67
            },
            "weather": [
                {
                    "id": 803,
                    "main": "Clouds",
                    "description": "broken clouds",
                    "icon": "04d"
                }
            ],
            "clouds": {"all": 51},
            "wind": {"speed": 7.03, "deg": 65, "gust": 7.96},
            "visibility": 10000,
            "pop": 0,
            "sys": {"pod": "d"},
            "dt_txt": "2026-03-13 09:00:00"
        }
    ],
    "city": {
        "id": 7922173,
        "name": "Al Atabah",
        "coord": {"lat": 30.044, "lon": 31.2257},
        "country": "EG",
        "population": 0,
        "timezone": 7200,
        "sunrise": 1773374869,
        "sunset": 1773417699
    }
}
"""

private const val currentPayload = """
{
    "coord": {"lat": 30.044, "lon": 31.2257},
    "weather": [
        {"id": 803, "main": "Clouds", "description": "broken clouds", "icon": "04d"}
    ],
    "base": "stations",
    "main": {
        "temp": 291.41,
        "feels_like": 290.73,
        "temp_min": 291.41,
        "temp_max": 295.08,
        "pressure": 1012,
        "sea_level": 1012,
        "grnd_level": 1005,
        "humidity": 55,
        "temp_kf": -3.67
    },
    "visibility": 10000,
    "wind": {"speed": 7.03, "deg": 65, "gust": 7.96},
    "clouds": {"all": 51},
    "dt": 1773392400,
    "sys": {"country": "EG", "sunrise": 1773374869, "sunset": 1773417699},
    "timezone": 7200,
    "id": 7922173,
    "name": "Al Atabah",
    "cod": 200
}
"""