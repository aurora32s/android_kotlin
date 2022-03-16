package com.haman.aop_part6_chapter01.data.entity.impl

import android.net.Uri
import com.haman.aop_part6_chapter01.data.entity.Entity

data class ReviewEntity(
    override val id: Long,
    val title: String,
    val description: String,
    val grade: Int,
    val images: List<Uri>? = null
): Entity
