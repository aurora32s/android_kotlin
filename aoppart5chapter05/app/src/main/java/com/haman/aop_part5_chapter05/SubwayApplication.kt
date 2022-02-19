package com.haman.aop_part5_chapter05

import android.app.Application
import com.haman.aop_part5_chapter05.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class SubwayApplication: Application() {

    // koin 초기화
    override fun onCreate() {
        super.onCreate()
        // DSL?
        startKoin {
            androidLogger( // fun
                if (BuildConfig.DEBUG) {
                    Level.DEBUG
                } else {
                    Level.NONE
                }
            )
            androidContext(this@SubwayApplication)
            modules(appModule)
        }
    }
}