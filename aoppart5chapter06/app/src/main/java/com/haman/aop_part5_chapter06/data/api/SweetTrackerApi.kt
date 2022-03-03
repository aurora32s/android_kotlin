package com.haman.aop_part5_chapter06.data.api

import com.haman.aop_part5_chapter06.BuildConfig
import com.haman.aop_part5_chapter06.data.entity.ShippingCompanies
import com.haman.aop_part5_chapter06.data.entity.ShippingCompany
import com.haman.aop_part5_chapter06.data.entity.TrackingInformation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SweetTrackerApi {

    /**
     * 택배사 코드, 송장 번호 기반의 정보
     */
    @GET("api/v1/trackingInfo?t_key=${BuildConfig.SWEET_TRACKER_API_KEY}")
    suspend fun getTrackingInformation(
        @Query("t_code") companyCode: String, // 택배사 코드
        @Query("t_invoice") invoice: String // 송장 번호
    ): Response<TrackingInformation>

    /**
     * 회사 목록(일주일 마다 회사 정보 update)
     */
    @GET("api/v1/companylist?t_key=${BuildConfig.SWEET_TRACKER_API_KEY}")
    suspend fun getShippingCompanies(): Response<ShippingCompanies>

    @GET("api/v1/recommend?t_key=${BuildConfig.SWEET_TRACKER_API_KEY}")
    suspend fun getRecommendShippingCompanies(
        @Query("t_invoice") invoice: String // 송장 번호
    ): Response<ShippingCompanies>

}