package com.haman.aop_part6_chapter01.data.repository.review

import com.haman.aop_part6_chapter01.data.entity.impl.ReviewEntity

interface RestaurantReviewRepository {

    suspend fun getReviews(restaurantTitle: String): List<ReviewEntity>

}