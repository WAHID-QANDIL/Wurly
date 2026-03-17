package com.wahid.wurly.data.local.database.converter

import androidx.room.TypeConverter
import com.wahid.wurly.data.common.model.Clouds
import com.wahid.wurly.data.common.model.Coordinates
import com.wahid.wurly.data.common.model.Main
import com.wahid.wurly.data.common.model.Weather
import com.wahid.wurly.data.common.model.Wind
import com.wahid.wurly.data.local.database.entity.City
import com.wahid.wurly.data.remote.api.dto.model.forecast_dayweather.Sys
import kotlinx.serialization.json.Json

class Converters {

    @TypeConverter fun fromCity(city: City): String  = Json.encodeToString(value = city)
    @TypeConverter fun fromClouds(clouds: Clouds): String = Json.encodeToString(value = clouds)
    @TypeConverter fun fromMain(main: Main): String = Json.encodeToString(value = main)
    @TypeConverter fun fromSys(sys: Sys): String = Json.encodeToString(value = sys)
    @TypeConverter fun fromWind(wind: Wind): String = Json.encodeToString(value = wind)
    @TypeConverter fun fromCoordinates(coordinates: Coordinates): String = Json.encodeToString(value = coordinates)
    @TypeConverter fun fromWeather(weatherList: Weather): String = Json.encodeToString(value = weatherList)


    @TypeConverter fun toCity(cityString: String): City = Json.decodeFromString(cityString)
    @TypeConverter fun toClouds(cloudsString: String): Clouds = Json.decodeFromString(cloudsString)
    @TypeConverter fun toMain(mainString: String): Main = Json.decodeFromString(mainString)
    @TypeConverter fun toSys(sysString: String): Sys = Json.decodeFromString(sysString)
    @TypeConverter fun toWind(windString: String): Wind = Json.decodeFromString(windString)
    @TypeConverter fun toCoordinates(coordinatesString: String): Coordinates = Json.decodeFromString(coordinatesString)
    @TypeConverter fun toWeather(weatherListString: String): Weather = Json.decodeFromString(weatherListString)

}