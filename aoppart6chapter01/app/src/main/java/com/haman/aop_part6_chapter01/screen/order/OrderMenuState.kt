package com.haman.aop_part6_chapter01.screen.order

import androidx.annotation.StringRes
import com.haman.aop_part6_chapter01.model.food.FoodModel

sealed interface OrderMenuState{

    object UnInitialized: OrderMenuState

    object Loading: OrderMenuState

    data class Success(
        val restaurantFoodModelIst: List<FoodModel>? = null
    ): OrderMenuState

    object Order: OrderMenuState

    data class Error(
        @StringRes val messageId: Int,
        val e : Throwable
    ): OrderMenuState

}