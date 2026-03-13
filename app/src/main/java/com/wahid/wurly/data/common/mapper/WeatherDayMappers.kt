package com.wahid.wurly.data.common.mapper

import com.wahid.wurly.data.local.database.entity.City
import com.wahid.wurly.data.local.database.entity.ForecastDayWeather
import com.wahid.wurly.data.remote.api.dto.model.dayweather.Sys
import com.wahid.wurly.domain.model.DayWeather
import com.wahid.wurly.domain.model.ForecastDays
import com.wahid.wurly.utils.ExtUtils.toDateAndTime

object WeatherDayMappers {

    fun ForecastDayWeather.toDomain() = ForecastDays(
        city = city.toDomainCity(),
        list = dayWeatherList.map {
            it.toDomainWeather(
                sys = Sys(
                    country = city.country,
                    sunrise = city.sunrise,
                    sunset = city.sunset,
                ),
                cityName = city.name
            )
        }
    )

    fun com.wahid.wurly.data.remote.api.dto.DayWeather.toDomainDayWeather(): DayWeather {
        val main = main
        return DayWeather(
            dayTime = dayTime,
            cityId = id,
            cityName = cityName,
            all = clouds.all,
            dayTimeString = dayTime.toDateAndTime(),
            feelsLike = main.feels_like,
            grandLevel = main.grnd_level,
            humidity = main.humidity,
            pressure = main.pressure,
            seaLevel = main.sea_level,
            temp = main.temp,
            tempKf = main.temp_kf,
            tempMax = main.temp_max,
            tempMin = main.temp_min,
            visibility = visibility,
            windDirectionDegrees = wind.windDirectionDegrees,
            windGust = wind.windGust ?: 0.0,
            windSpeed = wind.windSpeed,
            sys = sys,
        )
    }


    fun com.wahid.wurly.data.local.database.entity.DayWeather.toDomainWeather(sys: Sys,cityName: String): DayWeather {
        val main = main
        return DayWeather(
            dayTime = dayTime,
            cityName = cityName,
            cityId = cityId,
            all = clouds.all,
            dayTimeString = dayTime.toDateAndTime(),
            feelsLike = main.feels_like,
            grandLevel = main.grnd_level,
            humidity = main.humidity,
            pressure = main.pressure,
            seaLevel = main.sea_level,
            temp = main.temp,
            tempKf = main.temp_kf,
            tempMax = main.temp_max,
            tempMin = main.temp_min,
            visibility = visibility,
            windDirectionDegrees = wind.windDirectionDegrees,
            windGust = wind.windGust ?: 0.0,
            windSpeed = wind.windSpeed,
            sys = sys
        )
    }


    fun City.toDomainCity(): com.wahid.wurly.domain.model.City = com.wahid.wurly.domain.model.City(
        id = id,
        country = country,
        name = name,
        population = population,
        sunrise = sunrise,
        sunset = sunset,
        timezone = timezone,
        latitude = coordinates.latitude,
        longitude = coordinates.longitude
    )

}