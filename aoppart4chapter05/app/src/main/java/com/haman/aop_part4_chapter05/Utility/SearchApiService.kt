package com.haman.aop_part4_chapter05.Utility

import com.haman.aop_part4_chapter05.model.entity.GithubRepoEntity
import com.haman.aop_part4_chapter05.model.response.SearchRepoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchApiService {

    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String
    ): Response<SearchRepoResponse>

    @GET("repos/{owner}/{name}")
    suspend fun getRepository(
        @Path("owner") ownerLogin: String,
        @Path("name") repoName: String
    ): Response<GithubRepoEntity>
}