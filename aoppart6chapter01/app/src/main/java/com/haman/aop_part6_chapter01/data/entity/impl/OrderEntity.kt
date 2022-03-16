package com.haman.aop_part6_chapter01.data.entity.impl

data class OrderEntity(
    val id: String,
    val userId: String,
    val restaurantId: Long,
    val foodMenuList: List<RestaurantFoodEntity>
)
