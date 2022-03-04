package com.haman.aop_part5_chapter07.domain.usecase

import com.haman.aop_part5_chapter07.data.repository.MovieRepository
import com.haman.aop_part5_chapter07.data.repository.ReviewRepository
import com.haman.aop_part5_chapter07.domain.model.FeaturedMovie

/**
 * 추첨 영화 요청 Use Case
 */
class GetRandomFeaturedMovieUseCase(
    private val movieRepository: MovieRepository,
    private val reviewRepository: ReviewRepository
) {

    suspend operator fun invoke(): FeaturedMovie? {
        val featuredMovies = movieRepository.getAllMovies()
            .filter { it.id.isNullOrBlank().not() } // 방어코드
            .filter { it.isFeatured == true } // 추천영화 filtering

        if (featuredMovies.isNullOrEmpty())
            return null
        return featuredMovies.random() // random 하게 하나 추출
            .let { movie ->
                // 최근 댓글 정보 요청
                val latestReview = reviewRepository.getLatestReview(movie.id!!)
                FeaturedMovie(movie, latestReview)
            }
    }

}