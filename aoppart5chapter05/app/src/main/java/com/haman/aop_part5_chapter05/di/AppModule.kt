package com.haman.aop_part5_chapter05.di

import android.app.Activity
import com.google.firebase.ktx.BuildConfig
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.haman.aop_part5_chapter05.data.api.StationApi
import com.haman.aop_part5_chapter05.data.api.StationArrivalsApi
import com.haman.aop_part5_chapter05.data.api.StationStorageApi
import com.haman.aop_part5_chapter05.data.api.Url
import com.haman.aop_part5_chapter05.data.db.AppDatabase
import com.haman.aop_part5_chapter05.data.repository.StationRepository
import com.haman.aop_part5_chapter05.data.repository.StationRepositoryImpl
import com.haman.aop_part5_chapter05.data.preference.PreferenceManager
import com.haman.aop_part5_chapter05.data.preference.SharedPreferenceManager
import com.haman.aop_part5_chapter05.presentation.stationArrivals.StationArrivalsContract
import com.haman.aop_part5_chapter05.presentation.stationArrivals.StationArrivalsPresenter
import com.haman.aop_part5_chapter05.presentation.stationArrivals.StationsArrivalsFragment
import com.haman.aop_part5_chapter05.presentation.stations.StationContract
import com.haman.aop_part5_chapter05.presentation.stations.StationPresenter
import com.haman.aop_part5_chapter05.presentation.stations.StationsFragment
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.logging.Level

// DSL
val appModule = module {

    single { Dispatchers.IO }

    // Database
    single { AppDatabase.build(androidApplication()) }
    // 위에서 등록한 AppDatabase 를 가지고 와서(get()) stationDao 를 가지고 온다.
    single { get<AppDatabase>().stationDao() }

    // Preference
    single { androidContext().getSharedPreferences("prefernce", Activity.MODE_PRIVATE) }
    single<PreferenceManager> { SharedPreferenceManager(get()) }

    // Api
    single<StationApi> { StationStorageApi(Firebase.storage) }
    single {
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
    single<StationArrivalsApi> {
        Retrofit.Builder().baseUrl(Url.SEOUL_DATA_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create(StationArrivalsApi::class.java)
    }

    // Repository
    single<StationRepository> { StationRepositoryImpl(get(),get(),get(),get(),get()) }

    /**
     *Presentation
     * scope: create an object that persistent tied to the associated scope lifetime
     * 즉, presenter 가 StationFragment 의 생명주기에 영향을 받는다.
     * scope 내에서는 서로 공유가 가능하다.
     * 이 때, StationsFragment 가 ScopeFragment 를 상속 받아야 한다.
     */
    scope<StationsFragment> {
        scoped<StationContract.Presenter> { StationPresenter(getSource(), get()) }
    }
    scope<StationsArrivalsFragment> {
        scoped<StationArrivalsContract.Presenter> { StationArrivalsPresenter(getSource(), get(), get()) }
    }
}