package com.haman.aop_part6_chapter01.data.repository

import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantEntity
import com.haman.aop_part6_chapter01.screen.main.home.restaurant.RestaurantCategory

/**
 * repository base
 */
interface RestaurantRepository {

    // 모든 식당 리스트 요청
    suspend fun getList(
        restaurantCategory: RestaurantCategory
    ): List<RestaurantEntity>

}