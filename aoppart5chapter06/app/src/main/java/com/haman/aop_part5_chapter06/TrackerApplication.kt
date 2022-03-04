package com.haman.aop_part5_chapter06

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.haman.aop_part5_chapter06.di.appModule
import com.haman.aop_part5_chapter06.work.AppWorkerFactory
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class TrackerApplication: Application(), Configuration.Provider {

    private val workerFactory: AppWorkerFactory by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(
                if (BuildConfig.DEBUG) {
                    Level.ERROR
                } else {
                    Level.NONE
                }
            )
            androidContext(this@TrackerApplication)
            modules(appModule)
        }
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setMinimumLoggingLevel(
                if (BuildConfig.DEBUG) {
                    Log.DEBUG
                } else {
                    Log.INFO
                }
            )
            .setWorkerFactory(workerFactory)
            .build()

}