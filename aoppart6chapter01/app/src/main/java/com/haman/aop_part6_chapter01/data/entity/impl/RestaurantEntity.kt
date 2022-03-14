package com.haman.aop_part6_chapter01.data.entity.impl

import android.os.Parcelable
import com.haman.aop_part6_chapter01.data.entity.Entity
import com.haman.aop_part6_chapter01.screen.main.home.restaurant.RestaurantCategory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RestaurantEntity(
    override val id: Long, // diffUtil 에 사용
    val restaurantInfoId: Long, // api
    val restaurantCategory: RestaurantCategory,
    val restaurantTitle: String,
    val restaurantImageUrl: String,
    val grade: Float,
    val reviewCount: Int,
    val deliveryTimeRange: Pair<Int, Int>,
    val deliveryTipRange: Pair<Int, Int>
) : Entity, Parcelable
