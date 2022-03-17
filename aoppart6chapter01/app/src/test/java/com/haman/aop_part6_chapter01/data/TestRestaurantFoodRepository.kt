package com.haman.aop_part6_chapter01.data

import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantFoodEntity
import com.haman.aop_part6_chapter01.data.repository.food.RestaurantFoodRepository

class TestRestaurantFoodRepository: RestaurantFoodRepository {

    private val foodMenuListInBasket = mutableListOf<RestaurantFoodEntity>()

    override suspend fun getFoods(
        restaurantId: Long,
        restaurantTitle: String
    ): List<RestaurantFoodEntity> {
        return foodMenuListInBasket.filter {
            it.restaurantId == restaurantId &&
                    it.restaurantTitle == restaurantTitle
        }
    }

    override suspend fun getAllFoodMenuListInBasket(): List<RestaurantFoodEntity> {
        return foodMenuListInBasket
    }

    override suspend fun getFoodMenuListInBasket(restaurantId: Long): List<RestaurantFoodEntity> {
        return foodMenuListInBasket.filter{ it.restaurantId == restaurantId }
    }

    override suspend fun insertFoodMenuInBasket(restaurantFoodEntity: RestaurantFoodEntity) {
        foodMenuListInBasket.add(restaurantFoodEntity)
    }

    override suspend fun removeFoodMenuListInBasket(foodId: String) {
    }

    override suspend fun clearFoodMenuListInBasket() {
        foodMenuListInBasket.clear()
    }
}