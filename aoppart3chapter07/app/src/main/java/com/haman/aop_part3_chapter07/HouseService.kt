package com.haman.aop_part3_chapter07

import retrofit2.Call
import retrofit2.http.GET

interface HouseService {
    @GET("/v3/ec025c2a-b4f5-46ad-b803-4477455f2fff")
    fun getHouseList () : Call<HouseDto>
}