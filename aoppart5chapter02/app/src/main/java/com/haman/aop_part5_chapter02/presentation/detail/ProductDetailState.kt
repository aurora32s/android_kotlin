package com.haman.aop_part5_chapter02.presentation.detail

import com.haman.aop_part5_chapter02.data.entity.product.ProductEntity

sealed class ProductDetailState {

    object Uninitialized: ProductDetailState()
    object Loading: ProductDetailState()

    data class Success(
        val productEntity: ProductEntity
    ): ProductDetailState()

    object Order: ProductDetailState()
    object Error: ProductDetailState()
}