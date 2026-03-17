package com.wahid.wurly.work

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import com.wahid.wurly.presentation.framwork.work.DailyWeatherSyncWorker

/**
 * Helper to enqueue alert workers from the alerts screen.
 */
object WeatherAlertScheduler {
    private const val UNIQUE_ALERT_WORK = "weather_alert_work"

    fun scheduleAlert(context: Context) {
        val periodicRequest = PeriodicWorkRequestBuilder<WeatherAlertWorker>(25, TimeUnit.MINUTES).build()
        val workManager = WorkManager.getInstance(context)

        workManager.enqueueUniquePeriodicWork(
            UNIQUE_ALERT_WORK,
            ExistingPeriodicWorkPolicy.UPDATE,
            periodicRequest
        )

        val immediateRequest = androidx.work.OneTimeWorkRequestBuilder<WeatherAlertWorker>().build()
        workManager.enqueueUniqueWork(
            "immediate_alert_check",
            androidx.work.ExistingWorkPolicy.REPLACE,
            immediateRequest
        )
    }

    fun cancel(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(UNIQUE_ALERT_WORK)
    }

    fun scheduleDailySync(context: Context) {
        val periodicRequest = PeriodicWorkRequestBuilder<DailyWeatherSyncWorker>(24, TimeUnit.HOURS).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            DailyWeatherSyncWorker.UNIQUE_SYNC_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicRequest
        )
    }
}