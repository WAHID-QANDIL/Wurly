package com.wahid.wurly.presentation.framwork.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.wahid.wurly.domain.repository.WeatherRepository
import com.wahid.wurly.presentation.framwork.location.LocationServiceProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withTimeoutOrNull

/**
 * Worker that runs daily to fetch the latest weather conditions and populates the local Room database.
 * This ensures the cache is kept warm for the Home Screen without requiring blocking network calls
 * when the user opens the app.
 */
@HiltWorker
class DailyWeatherSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val repository: WeatherRepository,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        Log.d("DailyWeatherSync", "doWork: Started background weather synchronization")

        LocationServiceProvider.initializeLocationProvider()
        val deviceLocation = withTimeoutOrNull(5000L) {
            LocationServiceProvider.getLastLocation()
        }

        val filters = if (deviceLocation != null) {
            mapOf("lat" to deviceLocation.latitude.toString(), "lon" to deviceLocation.longitude.toString())
        } else {
            val forecast = repository.getNextForecastDays().firstOrNull()
            val city = forecast?.city ?: run {
                Log.e("DailyWeatherSync", "doWork: No location and no cached city to sync. Retrying later.")
                return Result.retry()
            }
            mapOf("lat" to city.latitude.toString(), "lon" to city.longitude.toString())
        }
        
        try {
            repository.getForecastDaysWeather(
                filters = filters,
                isFavorite = false,
                forceRefresh = true
            ).firstOrNull()
            Log.d("DailyWeatherSync", "doWork: Successfully refreshed daily weather cache")
            return Result.success()
        } catch (e: Exception) {
            Log.e("DailyWeatherSync", "doWork: Failed to sync weather cache", e)
            return Result.retry()
        }
    }

    companion object {
        const val UNIQUE_SYNC_WORK = "daily_weather_sync_work"
    }
}