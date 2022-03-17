package com.haman.aop_part6_chapter01.data.repository.review

import android.net.Uri
import com.haman.aop_part6_chapter01.data.entity.impl.ReviewEntity

interface RestaurantReviewRepository {

    suspend fun getReviews(restaurantTitle: String): DefaultRestaurantReviewRepository.Result
    suspend fun insertReview(reviewEntity: ReviewEntity): DefaultRestaurantReviewRepository.Result
    suspend fun insertReviewImages(imageUrlList: List<Uri>): DefaultRestaurantReviewRepository.Result

}