package com.wahid.wurly.data.location

import android.util.Log
import com.mapbox.common.location.Location
import com.mapbox.common.location.LocationObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class LocationObserverHandler(
    val onReceivedLocation:suspend (locations: List<Location?>) -> Unit,
    private val coroutineScope: CoroutineScope
) : LocationObserver {
    companion object {
        private const val TAG = "LocationObserver"
    }

    override fun onLocationUpdateReceived(locations: List<Location?>) {
        Log.d(
            TAG,
            "onLocationUpdateReceived: Received location update: ${locations.joinToString(", ")}"
        )

        coroutineScope.launch {
            onReceivedLocation(locations)
        }

    }
}