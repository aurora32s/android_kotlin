package com.haman.aop_part5_chapter02.presentation.list

import com.haman.aop_part5_chapter02.data.entity.product.ProductEntity

sealed class ProductListState {

    object Uninitialized: ProductListState()
    object Loading: ProductListState()

    data class Success(
        val productList: List<ProductEntity>
    ): ProductListState()

    object Error: ProductListState()

}