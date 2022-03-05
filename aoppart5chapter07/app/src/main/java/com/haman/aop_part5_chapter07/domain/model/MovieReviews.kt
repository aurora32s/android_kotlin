package com.haman.aop_part5_chapter07.domain.model

data class MovieReviews(
    val myReview: Review?, // 내가 작성한 리뷰
    val othersReview: List<Review> // 다른 사용자가 작성한 리뷰
)