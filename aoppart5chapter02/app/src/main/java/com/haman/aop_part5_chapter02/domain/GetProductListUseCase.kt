package com.haman.aop_part5_chapter02.domain

import com.haman.aop_part5_chapter02.data.entity.product.ProductEntity
import com.haman.aop_part5_chapter02.data.repository.ProductRepository

class GetProductListUseCase(
    private val productRepository: ProductRepository
): UseCase {

    suspend operator fun invoke(): List<ProductEntity> {
        return productRepository.getProductList()
    }
}