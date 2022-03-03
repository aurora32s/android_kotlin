package com.haman.aop_part5_chapter06.di

import com.haman.aop_part5_chapter06.BuildConfig
import com.haman.aop_part5_chapter06.data.api.SweetTrackerApi
import com.haman.aop_part5_chapter06.data.api.Url
import com.haman.aop_part5_chapter06.data.db.AppDatabase
import com.haman.aop_part5_chapter06.data.repository.TrackingItemRepository
import com.haman.aop_part5_chapter06.data.repository.TrackingItemRepositoryImpl
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

val appModule = module {

    // dispatcher
    single { Dispatchers.IO }

    // Database, Dao
    single { AppDatabase.build(androidApplication()) }
    single { get<AppDatabase>().trackingItemDao() }

    // Api
    single { // okhttp3 client
        OkHttpClient()
            .newBuilder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()
    }
    single<SweetTrackerApi> { // retrofit builder
        Retrofit.Builder().baseUrl(Url.SWEET_TRACKER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get()) // okhttp3 client
            .build()
            .create()
    }

    // Repository
    single<TrackingItemRepository> {
        // api, dao, dispatcher
        TrackingItemRepositoryImpl(get(),get(),get())
    }
}