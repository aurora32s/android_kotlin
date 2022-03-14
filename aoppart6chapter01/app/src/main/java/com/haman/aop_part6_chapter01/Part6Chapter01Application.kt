package com.haman.aop_part6_chapter01

import android.app.Application
import android.content.Context
import com.haman.aop_part6_chapter01.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Part6Chapter01Application: Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this

        startKoin {
            androidContext(this@Part6Chapter01Application)
            modules(appModule)
        }
    }

    /**
     * App 종료 시
     */
    override fun onTerminate() {
        super.onTerminate()
        appContext = null
    }

    companion object {
        var appContext: Context? = null
            private set
    }

}