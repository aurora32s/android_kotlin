package com.haman.aop_part6_chapter01.data.repository.food

import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantFoodEntity

interface RestaurantFoodRepository {

    // 특정 식당의 메뉴 리스트
    suspend fun getFoods(restaurantId: Long): List<RestaurantFoodEntity>
    // 장바구니에 있는 모든 메뉴 요청
    suspend fun getAllFoodMenuListInBasket(): List<RestaurantFoodEntity>
    // 장바구니에 있는 특정 식당의 메뉴 요청
    suspend fun getFoodMenuListInBasket(restaurantId: Long): List<RestaurantFoodEntity>
    // 장바구니에 추가
    suspend fun insertFoodMenuInBasket(restaurantFoodEntity: RestaurantFoodEntity)
    // 장바구니에서 특정 메뉴 제거
    suspend fun removeFoodMenuListInBasket(foodId: String)
    // 장바구니 비우기
    suspend fun clearFoodMenuListInBasket()

}