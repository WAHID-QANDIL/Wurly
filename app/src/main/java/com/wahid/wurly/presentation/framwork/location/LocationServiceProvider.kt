package com.wahid.wurly.presentation.framwork.location

import android.util.Log
import com.mapbox.common.location.AccuracyLevel
import com.mapbox.common.location.DeviceLocationProvider
import com.mapbox.common.location.IntervalSettings
import com.mapbox.common.location.Location
import com.mapbox.common.location.LocationProviderRequest
import com.mapbox.common.location.LocationService
import com.mapbox.common.location.LocationServiceFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


object LocationServiceProvider {

    private const val TAG = "LocationService"
    private lateinit var locationService: LocationService
    private lateinit var locationProvider: DeviceLocationProvider
    private var isLocationInitialized = false

    private lateinit var request: LocationProviderRequest
    private val locationObserver = LocationObserverHandler(
        onReceivedLocation = {
            onReceivedLocation(it)
        },
        coroutineScope = CoroutineScope(Dispatchers.IO)
    )
    private val onReceivedLocation: suspend (locations: List<Location?>) -> Unit = { locations ->
        Log.d(TAG, "Received location update: ${locations.joinToString(", ")}")
        delay(5000)
    }


    fun initializeLocationProvider() {
        if (!::locationService.isInitialized) {
            locationService = LocationServiceFactory.getOrCreate()
        }

        if (isLocationInitialized) {
            Log.d(TAG, "initializeLocationProvider: Location observer already added")
            return
        }

        request = LocationProviderRequest.Builder()
            .interval(
                IntervalSettings.Builder().interval(0L).minimumInterval(0L).maximumInterval(0L)
                    .build()
            )
            .displacement(0F)
            .accuracy(AccuracyLevel.HIGHEST)
            .build()

        val result = locationService.getDeviceLocationProvider(request)
        val provider = result.value
        if (provider == null) {
            Log.e(TAG, "initializeLocationProvider: Failed to get DeviceLocationProvider: ${result.error}")
            return
        }

        locationProvider = provider
        locationProvider.addLocationObserver(locationObserver)
        isLocationInitialized = true
        Log.d(TAG, "initializeLocationProvider: Location observer added")
    }

    suspend fun getLastLocation(): Location? = suspendCancellableCoroutine { cont ->
        if (!::locationProvider.isInitialized) {
            Log.w(TAG, "getLastLocation: provider not initialized")
            cont.resume(null)
            return@suspendCancellableCoroutine
        }
        locationProvider.getLastLocation { location ->
            cont.resume(location)
            location?.let { Log.d(TAG, "getLastLocation: Received last location: $it") }
                ?: Log.d(TAG, "getLastLocation: No last location available")
        }
    }

     fun stopLocationUpdates() {
        if (::locationProvider.isInitialized) {
            locationProvider.removeLocationObserver(locationObserver)
            isLocationInitialized = false
            Log.d(TAG, "stopLocationUpdates: Location updates stopped")
        } else {
            Log.w(TAG, "stopLocationUpdates: provider not initialized")
        }
    }
}