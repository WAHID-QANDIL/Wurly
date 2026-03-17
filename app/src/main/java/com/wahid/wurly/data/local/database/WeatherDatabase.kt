package com.wahid.wurly.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wahid.wurly.data.local.database.converter.Converters
import com.wahid.wurly.data.local.database.dao.WeatherDao
import com.wahid.wurly.data.local.database.entity.City
import com.wahid.wurly.data.local.database.entity.DayWeather


@Database(entities = [DayWeather::class, City::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun getWeatherDao(): WeatherDao
}