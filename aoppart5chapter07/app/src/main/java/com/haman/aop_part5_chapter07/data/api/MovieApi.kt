package com.haman.aop_part5_chapter07.data.api

import com.haman.aop_part5_chapter07.domain.model.Movie

/**
 * 영화 정보 요청 Api ( from fire store )
 */
interface MovieApi {
    suspend fun getAllMovies(): List<Movie>
    suspend fun getMovies(movieIds: List<String>): List<Movie>
}