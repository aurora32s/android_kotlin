package com.haman.aop_part6_chapter01.model.order

import com.haman.aop_part6_chapter01.data.entity.impl.OrderEntity
import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantFoodEntity
import com.haman.aop_part6_chapter01.model.CellType
import com.haman.aop_part6_chapter01.model.Model

data class OrderModel(
    override val id: Long,
    override val type: CellType = CellType.ORDER_CELL,
    val orderId: String,
    val userId: String,
    val restaurantId: Long,
    val foodMenuList: List<RestaurantFoodEntity>
): Model(id, type) {

    fun toEntity() = OrderEntity(
        id = orderId,
        userId = userId,
        restaurantId = restaurantId,
        foodMenuList = foodMenuList
    )

}
