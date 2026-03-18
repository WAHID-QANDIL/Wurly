package com.wahid.wurly.domain.connectivity

import kotlinx.coroutines.flow.Flow

/**
 * Emits true while the device has validated internet connectivity.
 */
interface ConnectivityObserver {
    val isConnected: Flow<Boolean>
}