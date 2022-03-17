package com.haman.aop_part6_chapter01.screen.main.home.restaurant.detail.review

import com.haman.aop_part6_chapter01.data.entity.impl.ReviewEntity
import com.haman.aop_part6_chapter01.model.review.ReviewModel

sealed interface RestaurantReviewState{

    object UnInitialized: RestaurantReviewState

    object Loading: RestaurantReviewState

    data class Success(
        val reviewList: List<ReviewModel>
    ): RestaurantReviewState

    data class Error(
        val exception: Throwable
    ): RestaurantReviewState

}