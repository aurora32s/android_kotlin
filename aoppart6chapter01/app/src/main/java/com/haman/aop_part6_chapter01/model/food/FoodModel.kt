package com.haman.aop_part6_chapter01.model.food

import com.haman.aop_part6_chapter01.model.CellType
import com.haman.aop_part6_chapter01.model.Model

data class FoodModel(
    override val id: Long,
    override val type: CellType = CellType.FOOD_CELL,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    val restaurantId: Long
): Model(id, type)