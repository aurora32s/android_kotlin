package com.haman.aop_part6_chapter01.model.food

import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantFoodEntity
import com.haman.aop_part6_chapter01.model.CellType
import com.haman.aop_part6_chapter01.model.Model

data class FoodModel(
    override val id: Long, // for model
    override val type: CellType = CellType.FOOD_CELL,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    val restaurantId: Long,
    val foodId: String,
    val restaurantTitle: String
): Model(id, type) {

    fun toEntity(basketIndex: Int) = RestaurantFoodEntity(
        "${foodId}_${basketIndex}",
        title,
        description,
        price,
        imageUrl,
        restaurantId,
        restaurantTitle
    )

}