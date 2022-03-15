package com.haman.aop_part6_chapter01.data.response.restaurant

import androidx.room.PrimaryKey
import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantFoodEntity

data class RestaurantFoodResponse(
    val id: String,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String
) {
    fun toEntity(restaurantId: Long) = RestaurantFoodEntity(
        id,
        title,
        description,
        price.toDouble().toInt(),
        imageUrl,
        restaurantId
    )
}