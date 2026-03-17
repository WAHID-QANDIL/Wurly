package com.wahid.wurly.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.Manifest
import android.content.pm.PackageManager
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.wahid.wurly.presentation.screen.alerts.AlertCase
import com.wahid.wurly.data.location.LocationServiceProvider
import com.wahid.wurly.domain.repository.WeatherRepository
import com.wahid.wurly.presentation.screen.alerts.AlertStyle
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withTimeoutOrNull
import android.util.Log
import com.wahid.wurly.domain.model.weather.City
import com.wahid.wurly.R

/**
 * WorkManager worker that periodically checks current weather conditions against all enabled alerts.
 * If any conditions are met, it triggers a standard system notification.
 */
@HiltWorker
class WeatherAlertWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val repository: WeatherRepository,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        Log.d("WeatherAlertWorker", "doWork: Started worker execution")
        val currentTime = System.currentTimeMillis()
        val alerts = repository.observeAlerts().first().filter { alert ->
            alert.enabled && currentTime <= alert.createdAt + alert.durationMillis
        }
        if (alerts.isEmpty()) {
            Log.d("WeatherAlertWorker", "doWork: No active alerts found, skipping")
            return Result.success()
        }

        Log.d("WeatherAlertWorker", "doWork: Found ${alerts.size} active alerts")

        LocationServiceProvider.initializeLocationProvider()
        val deviceLocation = withTimeoutOrNull(5000L) {
            LocationServiceProvider.getLastLocation()
        }

        Log.d("WeatherAlertWorker", "doWork: Device location resolved: $deviceLocation")

        val filters = if (deviceLocation != null) {
            mapOf("lat" to deviceLocation.latitude.toString(), "lon" to deviceLocation.longitude.toString())
        } else {
            val forecast = repository.getNextForecastDays().firstOrNull()
            val city: City = forecast?.city ?: run {
                Log.e("WeatherAlertWorker", "doWork: No location and no cached city. Retrying.")
                return Result.retry()
            }
            Log.d("WeatherAlertWorker", "doWork: Falling back to cached city: ${city.name} (${city.latitude}, ${city.longitude})")
            mapOf("lat" to city.latitude.toString(), "lon" to city.longitude.toString())
        }

        val currentDay = repository.getCurrentDayWeather(filters).firstOrNull() ?: run {
            Log.e("WeatherAlertWorker", "doWork: Failed to fetch current day weather. Retrying.")
            return Result.retry()
        }

        Log.d("WeatherAlertWorker", "doWork: Fetched weather - Temp: ${currentDay.temp}, MaxTemp: ${currentDay.tempMax}, Desc: ${currentDay.condition.description}")

        val desc = currentDay.condition.description.lowercase()
        val temp = currentDay.tempMax
        val windSpeed = currentDay.windSpeed
        val humidity = currentDay.humidity


        val triggeredAlerts = alerts.filter { alert ->
            val triggered = when (alert.alertCase) {
                AlertCase.Storm -> windSpeed >= 20.0 || desc.contains("storm") || desc.contains("thunder")
                AlertCase.HeatAdvisory -> temp >= 40.0
                AlertCase.FlashFloodWatch -> humidity >= 85.0 || desc.contains("rain") || desc.contains("flood")
                AlertCase.CustomTemperature -> alert.targetTemperature != null && currentDay.temp >= alert.targetTemperature
            }
            if (triggered) {
                Log.d("WeatherAlertWorker", "doWork: Alert triggered - Case: ${alert.alertCase}, TargetTemp: ${alert.targetTemperature}, CurrentTemp: ${currentDay.temp}")
            }
            triggered
        }

        if (triggeredAlerts.isEmpty()) {
             Log.d("WeatherAlertWorker", "doWork: Valid weather, but no alert thresholds were met.")
             return Result.success()
        }

        val style = if (triggeredAlerts.any { it.style == AlertStyle.Alarm }) {
            AlertStyle.Alarm
        } else {
            AlertStyle.Notification
        }


        postNotification(style)

        return Result.success()
    }

    private fun postNotification(style: AlertStyle) {
        val nm = applicationContext.getSystemService(NotificationManager::class.java) ?: return

        val channelId = if (style == AlertStyle.Alarm) ALARM_CHANNEL_ID else STANDARD_CHANNEL_ID

        val name = if (style == AlertStyle.Alarm) "Weather Alarms" else "Weather Alerts"
        val importance = if (style == AlertStyle.Alarm) NotificationManager.IMPORTANCE_HIGH else NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance)

        if (style == AlertStyle.Alarm) {
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            channel.setSound(uri, android.media.AudioAttributes.Builder()
                .setUsage(android.media.AudioAttributes.USAGE_ALARM)
                .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build())
        }
        nm.createNotificationChannel(channel)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        val title = applicationContext.getString(R.string.alerts_notification_title)
        val text = if (style == AlertStyle.Alarm) applicationContext.getString(R.string.alerts_notification_alarm) else applicationContext.getString(R.string.alerts_notification_standard)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)

        if (style == AlertStyle.Alarm) {
            notification.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
        }

        nm.notify(NOTIFICATION_ID, notification.build())
    }

    companion object {
        const val STANDARD_CHANNEL_ID = "weather_alerts_standard"
        const val ALARM_CHANNEL_ID = "weather_alerts_alarm"
        private const val NOTIFICATION_ID = 9101
    }
}