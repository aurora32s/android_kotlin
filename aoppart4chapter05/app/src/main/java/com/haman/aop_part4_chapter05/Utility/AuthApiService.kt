package com.haman.aop_part4_chapter05.Utility

import com.haman.aop_part4_chapter05.model.response.GithubAccessTokenResponse
import retrofit2.Response
import retrofit2.http.*

interface AuthApiService {

    @POST("login/oauth/access_token")
    @Headers("Accept: application/json")
    suspend fun getAccessToken(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("code") code: String
    ): Response<GithubAccessTokenResponse>

}