package com.wahid.wurly.di

import com.wahid.wurly.data.connectivity.ConnectivityObserverImpl
import com.wahid.wurly.domain.connectivity.ConnectivityObserver
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConnectivityModule {

    @Binds
    @Singleton
    abstract fun bindConnectivityObserver(
        impl: ConnectivityObserverImpl,
    ): ConnectivityObserver
}