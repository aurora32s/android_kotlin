package com.haman.aop_part6_chapter01.model.restaurant

import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantEntity
import com.haman.aop_part6_chapter01.model.CellType


fun RestaurantEntity.toModel(type: CellType): RestaurantModel {
    return RestaurantModel(
        id = id,
        type = type,
        restaurantInfoId = restaurantInfoId,
        restaurantCategory = restaurantCategory,
        restaurantTitle = restaurantTitle,
        restaurantImageUrl = restaurantImageUrl,
        grade = grade,
        reviewCount = reviewCount,
        deliveryTimeRange = deliveryTimeRange,
        deliveryTipRange = deliveryTipRange
    )
}

fun RestaurantModel.toEntity(): RestaurantEntity {
    return RestaurantEntity(
        id,
        restaurantInfoId,
        restaurantCategory,
        restaurantTitle,
        restaurantImageUrl,
        grade,
        reviewCount,
        deliveryTimeRange,
        deliveryTipRange
    )
}