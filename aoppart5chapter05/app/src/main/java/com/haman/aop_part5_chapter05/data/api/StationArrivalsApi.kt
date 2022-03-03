package com.haman.aop_part5_chapter05.data.api

import com.haman.aop_part5_chapter05.BuildConfig
import com.haman.aop_part5_chapter05.data.api.response.RealtimeStationArrivals
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface StationArrivalsApi {
    @GET("api/subway/${BuildConfig.SEOUL_API_ACCESS_KEY}/json/realtimeStationArrival/0/100/{stationName}")
    suspend fun getRealtimeStationArrival(
        @Path("stationName") stationName: String
    ): Response<RealtimeStationArrivals>
}