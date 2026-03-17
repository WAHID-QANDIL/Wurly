package com.wahid.wurly.di

import com.wahid.wurly.utils.AndroidResourceAccessor
import com.wahid.wurly.utils.ResourceAccessor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ResourceModule {

    @Binds
    @Singleton
    abstract fun bindResourceAccessor(impl: AndroidResourceAccessor): ResourceAccessor
}