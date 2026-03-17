package com.wahid.wurly.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wahid.wurly.data.local.database.converter.Converters
import com.wahid.wurly.data.local.database.dao.WeatherDao
import com.wahid.wurly.data.local.database.entity.City
import com.wahid.wurly.data.local.database.entity.DayWeather
import com.wahid.wurly.data.local.database.entity.WeatherAlertEntity


@Database(entities = [DayWeather::class, City::class, WeatherAlertEntity::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun getWeatherDao(): WeatherDao
}