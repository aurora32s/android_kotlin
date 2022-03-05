package com.haman.aop_part5_chapter07.data.api.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import com.haman.aop_part5_chapter07.data.api.ReviewApi
import com.haman.aop_part5_chapter07.domain.model.Movie
import com.haman.aop_part5_chapter07.domain.model.Review
import kotlinx.coroutines.tasks.await

class ReviewFireStoreApi(
    private val fireStore: FirebaseFirestore
) : ReviewApi {

    override suspend fun getLatestReview(movieId: String): Review? =
        fireStore.collection("reviews")
            .whereEqualTo("movieId", movieId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .await()
            .map { it.toObject<Review>() }
            .firstOrNull()

    override suspend fun getAllMovieReviews(movieId: String): List<Review> =
        fireStore.collection("reviews")
            .whereEqualTo("movieId", movieId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .await()
            .map { it.toObject() }

    override suspend fun getAllUserReviews(userId: String): List<Review> =
        fireStore.collection("reviews")
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .await()
            .map { it.toObject() }

    override suspend fun addReview(review: Review): Review {
        // transaction: 여러 작업을 하나의 세트로 정의
        // 도중에 에러가 발생한 경우, 이전에 작업한 작업 또한 rollback 된다.
        val newReviewReference = fireStore.collection("reviews").document() // document 생성
        val movieReference = fireStore.collection("movies").document( review.movieId!! )

        // 예외 발생 시 자체 exception throw
        fireStore.runTransaction { transition ->
            val movie = transition.get(movieReference).toObject<Movie>()!!

            val oldAverageScore = movie.averageScore ?: 0f
            val oldNumberOfScore = movie.numberOfScore ?: 0
            val oldTotalScore = oldAverageScore * oldNumberOfScore

            val newNumberOfScore = oldNumberOfScore + 1
            val newAverageScore = (oldTotalScore + (review.score ?: 0f)) / newNumberOfScore

            // movie 데이터 업데이트
            transition.set(
                movieReference,
                movie.copy(
                    numberOfScore = newNumberOfScore,
                    averageScore = newAverageScore
                )
            )

            transition.set(
                newReviewReference,
                review,
                SetOptions.merge()
            )
        }.await()

        return newReviewReference.get().await().toObject()!!
    }

    override suspend fun removeReview(review: Review) {
        val reviewReference = fireStore.collection("reviews").document(review.id!!)
        val movieReference = fireStore.collection("movies").document(review.movieId!!)

        fireStore.runTransaction { transition ->
            val movie = transition
                .get(movieReference)
                .toObject<Movie>()!!

            val oldAverageScore = movie.averageScore ?: 0f
            val oldNumberOfScore = movie.numberOfScore ?: 0
            val oldTotalScore = oldAverageScore * oldNumberOfScore

            val newNumberOfScore = (oldNumberOfScore - 1).coerceAtLeast(0)
            val newAverageScore = if (newNumberOfScore > 0) {
                    (oldTotalScore + (review.score ?: 0f)) / newNumberOfScore
                } else {
                    0f
                }

            // movie 데이터 업데이트
            transition.set(
                movieReference,
                movie.copy(
                    numberOfScore = newNumberOfScore,
                    averageScore = newAverageScore
                )
            )
            // 리뷰 삭제
            transition.delete(reviewReference)
        }.await()
    }
}