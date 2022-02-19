package com.haman.aop_part4_chapter03.utility

import com.haman.aop_part4_chapter03.BuildConfig
import com.haman.aop_part4_chapter03.Url
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitUtil {
    val apiService: ApiService by lazy { getRetrofit().create(ApiService::class.java) }

    private fun  getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Url.TMAP_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildOkHttpClient())
            .build()
    }

    private fun buildOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor (
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            ).build()
}