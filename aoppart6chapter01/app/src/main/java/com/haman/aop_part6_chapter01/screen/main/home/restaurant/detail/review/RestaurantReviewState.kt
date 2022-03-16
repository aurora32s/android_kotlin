package com.haman.aop_part6_chapter01.screen.main.home.restaurant.detail.review

import com.haman.aop_part6_chapter01.data.entity.impl.ReviewEntity

sealed interface RestaurantReviewState{

    object UnInitialized: RestaurantReviewState

    object Loading: RestaurantReviewState

    data class Success(
        val reviewList: List<ReviewEntity>
    ): RestaurantReviewState

}