package com.haman.aop_part5_chapter07.data.repository

import com.haman.aop_part5_chapter07.domain.model.Review

/**
 * 댓글 관리 repository
 */
interface ReviewRepository {
    suspend fun getLatestReview(movieId: String): Review?
    suspend fun getAllMovieReviews(movieId: String): List<Review>
    suspend fun getAllUserReviews(userId: String): List<Review>
    suspend fun addReview(review: Review): Review
    suspend fun removeReview(review: Review)
}