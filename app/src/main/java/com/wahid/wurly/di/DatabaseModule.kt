package com.wahid.wurly.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.wahid.wurly.data.local.database.WeatherDatabase
import com.wahid.wurly.data.local.database.converter.Converters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(value = [SingletonComponent::class])
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): WeatherDatabase {
        return Room.databaseBuilder(
                context,
                name = "weather_db",
                klass = WeatherDatabase::class.java
            ).fallbackToDestructiveMigration(true)
            .build()
    }

}