package com.haman.aop_part3_chapter04.model

import com.google.gson.annotations.SerializedName

// Data Transfer Object
data class BestSellerDto(
    @SerializedName("title") val title : String,
    @SerializedName("item") val books : List<Book>
)
