package com.haman.aop_part6_chapter01.data.repository.food

import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantFoodEntity

interface RestaurantFoodRepository {

    suspend fun getFoods(restaurantId: Long): List<RestaurantFoodEntity>

}