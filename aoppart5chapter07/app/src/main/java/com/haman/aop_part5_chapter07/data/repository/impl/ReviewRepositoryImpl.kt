package com.haman.aop_part5_chapter07.data.repository.impl

import com.haman.aop_part5_chapter07.data.api.ReviewApi
import com.haman.aop_part5_chapter07.data.repository.ReviewRepository
import com.haman.aop_part5_chapter07.domain.model.Review
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * 댓글 관리 repository implements
 */
class ReviewRepositoryImpl(
    private val reviewApi: ReviewApi,
    private val dispatcher: CoroutineDispatcher
) : ReviewRepository {

    override suspend fun getLatestReview(movieId: String): Review? = withContext(dispatcher) {
        reviewApi.getLatestReview(movieId)
    }

    override suspend fun getAllMovieReviews(movieId: String): List<Review> =
        withContext(dispatcher) {
            reviewApi.getAllMovieReviews(movieId)
        }

    override suspend fun getAllUserReviews(userId: String): List<Review> =
        withContext(dispatcher) {
            reviewApi.getAllUserReviews(userId)
        }

    override suspend fun addReview(review: Review): Review = withContext(dispatcher){
        reviewApi.addReview(review)
    }

    override suspend fun removeReview(review: Review) {
        withContext(dispatcher){
            reviewApi.addReview(review)
        }
    }
}