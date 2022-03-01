package com.haman.aop_part3_chapter06

import android.app.Application
import android.content.Context

class AopPart5Chapter04Application: Application() {

    companion object {
        var appContext: Context? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    override fun onTerminate() {
        super.onTerminate()
        appContext = null
    }
}