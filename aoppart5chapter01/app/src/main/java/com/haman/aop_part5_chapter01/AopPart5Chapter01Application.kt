package com.haman.aop_part5_chapter01

import android.app.Application
import android.util.Log
import com.haman.aop_part5_chapter01.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class AopPart5Chapter01Application : Application() {

    override fun onCreate() {
        super.onCreate()
        // TODO create Koin trigger
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@AopPart5Chapter01Application)
            modules(appModule)
        }
    }
}