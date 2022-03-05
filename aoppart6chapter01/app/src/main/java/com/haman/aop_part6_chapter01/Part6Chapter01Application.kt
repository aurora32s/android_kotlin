package com.haman.aop_part6_chapter01

import android.app.Application
import android.content.Context

class Part6Chapter01Application: Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this

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