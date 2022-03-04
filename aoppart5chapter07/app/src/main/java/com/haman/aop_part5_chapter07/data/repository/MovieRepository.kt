package com.haman.aop_part5_chapter07.data.repository

import com.haman.aop_part5_chapter07.domain.model.Movie

/**
 * 영화 관리 repository
 */
interface MovieRepository {
    /**
     * 모든 영화 정보 요청
     */
    suspend fun getAllMovies(): List<Movie>
    /**
     * 특정 영화 정보 요청
     */
    suspend fun getMovies(movieId: List<String>): List<Movie>
}