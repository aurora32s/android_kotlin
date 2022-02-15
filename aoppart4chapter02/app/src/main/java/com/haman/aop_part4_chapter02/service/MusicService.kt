package com.haman.aop_part4_chapter02.service

import retrofit2.Call
import retrofit2.http.GET

interface MusicService {

    @GET("/v3/5b9eaca4-5107-4329-b0b5-bb2076a3916e")
    fun getListMusics(): Call<MusicDto>
}