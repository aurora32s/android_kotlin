package com.haman.aop_part5_chapter02.domain

import com.haman.aop_part5_chapter02.data.repository.ProductRepository

internal class DeleteOrderedProductListUseCase(
    private val productRepository: ProductRepository
): UseCase {

    suspend operator fun invoke() {
        return productRepository.deleteAll()
    }
}