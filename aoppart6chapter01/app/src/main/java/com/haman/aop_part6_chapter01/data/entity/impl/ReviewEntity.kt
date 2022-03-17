package com.haman.aop_part6_chapter01.data.entity.impl

import android.net.Uri
import com.haman.aop_part6_chapter01.data.entity.Entity

data class ReviewEntity(
    override val id: Long,
    val userId: String,
    val title: String,
    val createdAt: Long,
    val content: String,
    val rating: Float,
    val imagesUrlList: List<String>? = null,
    val orderId: String,
    val restaurantTitle: String
): Entity
