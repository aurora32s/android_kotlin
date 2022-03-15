package com.haman.aop_part6_chapter01.data.repository.food

import android.util.Log
import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantFoodEntity
import com.haman.aop_part6_chapter01.data.network.FoodApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultRestaurantFoodRepository(
    private val foodApiService: FoodApiService,
    private val ioDispatcher: CoroutineDispatcher
): RestaurantFoodRepository {
    override suspend fun getFoods(restaurantId: Long): List<RestaurantFoodEntity> =
        withContext(ioDispatcher) {
            val response = foodApiService.getRestaurantFoods(restaurantId)

            if (response.isSuccessful) {
                response.body()?.map {
                    it.toEntity(restaurantId)
                } ?: listOf()
            } else {
                listOf()
            }
        }
}