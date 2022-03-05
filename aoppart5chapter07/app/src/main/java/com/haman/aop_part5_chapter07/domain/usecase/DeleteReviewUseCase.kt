package com.haman.aop_part5_chapter07.domain.usecase

import com.haman.aop_part5_chapter07.data.repository.ReviewRepository
import com.haman.aop_part5_chapter07.domain.model.Review

class DeleteReviewUseCase(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(review: Review) {
        reviewRepository.removeReview(review)
    }
}