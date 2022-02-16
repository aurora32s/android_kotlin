package com.haman.aop_part4_chapter03.utility

import com.haman.aop_part4_chapter03.Key
import com.haman.aop_part4_chapter03.Url
import com.haman.aop_part4_chapter03.model.response.search.SearchResponse
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(Url.GET_TMAP_LOCATION)
    suspend fun getSearchLocation(
        @Header("appKey") appKey: String = Key.TMAP_API,
        @Query("version") version: Int = 1,
        @Query("callback") callback: String? = null,
        @Query("count") count: Int = 1,
        @Query("searchKeyword") searchKeyword: String? = null,
        @Query("areaLLCode") areaLLCode: String? = null,
        @Query("areaLMCode") areaLMCode: String? = null,
        @Query("resCoordType") resCoordType: String? = null,
        @Query("searchType") searchType: String? = null,
        @Query("multiPoint") multiPoint: String? = null,
        @Query("searchtypCd") searchtypCd: String? = null,
        @Query("radius") radius: String? = null,
        @Query("reqCoordType") reqCoordType: String? = null,
        @Query("centerLon") centerLon: String? = null,
        @Query("centerLat") centerLat: String? = null,
    ): Response<SearchResponse>
}