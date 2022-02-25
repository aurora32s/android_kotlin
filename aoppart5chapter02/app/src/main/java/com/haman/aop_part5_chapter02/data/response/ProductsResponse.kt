package com.haman.aop_part5_chapter02.data.response

data class ProductsResponse(
    val items: List<ProductResponse>,
    val count: Int
)
