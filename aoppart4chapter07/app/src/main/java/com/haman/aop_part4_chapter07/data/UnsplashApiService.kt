package com.haman.aop_part4_chapter07.data

import com.haman.aop_part4_chapter07.BuildConfig
import com.haman.aop_part4_chapter07.data.models.PhotoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApiService {

    @GET(
        "photos/random?" +
                "client_id=${BuildConfig.UNSPLASH_ACCESS_KEY}" +
                "&count=30"
    )
    suspend fun getRandomPhoto(
        @Query("query") query: String?,
    ): Response<List<PhotoResponse>>
}