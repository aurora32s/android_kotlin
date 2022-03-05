package com.haman.aop_part5_chapter07.data.api.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.haman.aop_part5_chapter07.data.api.ReviewApi
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
}