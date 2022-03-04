package com.haman.aop_part5_chapter07.domain.model

/**
 * 추첨 영화(영화 정보, 최근 댓글)
 */
data class FeaturedMovie(
    val movie: Movie,
    val latestReview: Review?
)
