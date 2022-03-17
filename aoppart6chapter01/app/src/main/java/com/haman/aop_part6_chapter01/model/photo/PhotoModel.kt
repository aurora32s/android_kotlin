package com.haman.aop_part6_chapter01.model.photo

import android.net.Uri

data class PhotoModel(
    val id: Long,
    val uri: Uri,
    val name: String,
    val date: String,
    val size: Int,
    var isSelected: Boolean = false
)