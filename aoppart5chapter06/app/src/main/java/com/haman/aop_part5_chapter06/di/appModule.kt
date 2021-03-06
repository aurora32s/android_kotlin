package com.haman.aop_part5_chapter06.di

import android.app.Activity
import android.app.Application
import com.haman.aop_part5_chapter06.BuildConfig
import com.haman.aop_part5_chapter06.data.api.SweetTrackerApi
import com.haman.aop_part5_chapter06.data.api.Url
import com.haman.aop_part5_chapter06.data.db.AppDatabase
import com.haman.aop_part5_chapter06.data.entity.TrackingInformation
import com.haman.aop_part5_chapter06.data.entity.TrackingItem
import com.haman.aop_part5_chapter06.data.preference.PreferenceManager
import com.haman.aop_part5_chapter06.data.preference.SharedPreferenceManager
import com.haman.aop_part5_chapter06.data.repository.*
import com.haman.aop_part5_chapter06.presentation.addtrackingitem.AddTrackingItemFragment
import com.haman.aop_part5_chapter06.presentation.addtrackingitem.AddTrackingItemPresenter
import com.haman.aop_part5_chapter06.presentation.addtrackingitem.AddTrackingItemsContract
import com.haman.aop_part5_chapter06.presentation.trackinghistory.TrackingHistoryContract
import com.haman.aop_part5_chapter06.presentation.trackinghistory.TrackingHistoryFragment
import com.haman.aop_part5_chapter06.presentation.trackinghistory.TrackingHistoryPresenter
import com.haman.aop_part5_chapter06.presentation.trackingitems.TrackingItemsContract
import com.haman.aop_part5_chapter06.presentation.trackingitems.TrackingItemsFragment
import com.haman.aop_part5_chapter06.presentation.trackingitems.TrackingItemsPresenter
import com.haman.aop_part5_chapter06.work.AppWorkerFactory
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
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
    single { get<AppDatabase>().shippingCompanyDao() }

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

    // Preference
    single { androidContext().getSharedPreferences("preference", Activity.MODE_PRIVATE) }
    single<PreferenceManager> { SharedPreferenceManager(get()) }

    // Repository
    single<TrackingItemRepository> {
        // api, dao, dispatcher
        TrackingItemRepositoryImpl(get(),get(),get())
    }
//    single<TrackingItemRepository> { TrackingItemRepositoryStub() }
    single<ShippingCompanyRepository> {
        // api, dao, preference, dispatcher
        ShippingCompanyRepositoryImpl(get(),get(),get(),get())
    }

    // Work
    single { AppWorkerFactory(get(), get()) }

    // Presentation
    scope<TrackingItemsFragment> {
        scoped<TrackingItemsContract.Presenter> {
            TrackingItemsPresenter(getSource(), get())
        }
    }
    scope<AddTrackingItemFragment> {
        scoped<AddTrackingItemsContract.Presenter> {
            AddTrackingItemPresenter(getSource(),get(),get())
        }
    }
    scope<TrackingHistoryFragment> {
        scoped<TrackingHistoryContract.Presenter> { (item: TrackingItem, information: TrackingInformation) ->
            TrackingHistoryPresenter(getSource(), get(), item, information)
        }
    }
}