package com.haman.aop_part3_chapter08.service

import com.haman.aop_part3_chapter08.dto.VideoDto
import retrofit2.Call
import retrofit2.http.GET

interface VideoService {

    @GET("/v3/4e570bb3-3e6d-4cc6-a3d4-7ffcf211c01f")
    fun getVideos () : Call<VideoDto>
}