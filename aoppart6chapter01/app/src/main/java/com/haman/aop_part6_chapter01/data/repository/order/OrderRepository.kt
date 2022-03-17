package com.haman.aop_part6_chapter01.data.repository.order

import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantFoodEntity

interface OrderRepository {

    suspend fun orderMenu(
        userId: String,
        restaurantId: Long,
        foodMenuList: List<RestaurantFoodEntity>,
        restaurantTitle: String
    ): DefaultOrderRepository.Result

    suspend fun getAllOrderMenus(
        userId: String
    ): DefaultOrderRepository.Result

}