package com.wahid.wurly.di

import com.wahid.wurly.BuildConfig
import com.wahid.wurly.data.remote.api.WeatherApiService
import com.wahid.wurly.utils.Constants.APP_ID
import com.wahid.wurly.utils.KeyProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(value = [SingletonComponent::class])
object NetworkModule {


    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }


    @Provides
    @Singleton
    fun provideHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(loggingInterceptor)
            }
            addInterceptor { chain ->
                val request = chain.request()
                val url = request.url.newBuilder().apply {
                    addQueryParameter(APP_ID, KeyProvider.getAppId())
                }.build()
                val newRequest = request.newBuilder().url(url).build()
                chain.proceed(newRequest)
            }
            connectTimeout(3, TimeUnit.SECONDS)
            readTimeout(10, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
        }.build()
    }


    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            isLenient = true
            explicitNulls = false
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
       return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(KeyProvider.getBaseUrl())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofitService(retrofit: Retrofit): WeatherApiService {
        return retrofit.create<WeatherApiService>(WeatherApiService::class.java)
    }


}