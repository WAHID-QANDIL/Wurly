package com.wahid.wurly.presentation.framwork

import android.app.Activity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.mapbox.android.core.permissions.PermissionsManager
import com.wahid.wurly.presentation.framwork.location.LocationServiceProvider

class MainActivityLifecycleObserver(private val context: Activity) : DefaultLifecycleObserver {

    companion object {
        private lateinit var MainActivityLifecycleObserver: DefaultLifecycleObserver

        fun getInstance(context: Activity): DefaultLifecycleObserver {
            if (!::MainActivityLifecycleObserver.isInitialized) {
                MainActivityLifecycleObserver = MainActivityLifecycleObserver(context)
            }
            return MainActivityLifecycleObserver
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        if (PermissionsManager.areLocationPermissionsGranted(context)) {
            LocationServiceProvider.initializeLocationProvider()
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        LocationServiceProvider.stopLocationUpdates()
    }
}