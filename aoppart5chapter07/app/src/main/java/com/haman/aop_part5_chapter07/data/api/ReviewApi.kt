package com.haman.aop_part5_chapter07.data.api

import com.haman.aop_part5_chapter07.domain.model.Review

/**
 * 리뷰 정보 요청 Api ( from fire store )
 */
interface ReviewApi {
    suspend fun getLatestReview(movieId: String): Review?
    suspend fun getAllMovieReviews(movieId: String): List<Review>
}