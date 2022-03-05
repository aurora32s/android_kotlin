package com.haman.aop_part5_chapter07.domain.usecase

import com.haman.aop_part5_chapter07.data.repository.MovieRepository
import com.haman.aop_part5_chapter07.data.repository.ReviewRepository
import com.haman.aop_part5_chapter07.data.repository.UserRepository
import com.haman.aop_part5_chapter07.domain.model.ReviewedMovie
import com.haman.aop_part5_chapter07.domain.model.User

class GetMyReviewedMovieUseCase(
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository,
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(): List<ReviewedMovie> {
        val user = userRepository.getUser()

        if (user == null) {
            // 자동 회원가입
            userRepository.saveUser(User())
            return emptyList()
        }

        // 내가 작성한 댓글 리스트 요청
        val reviews = reviewRepository.getAllUserReviews(user.id!!)
            .filter { it.movieId.isNullOrBlank().not() }
        // 작성한 댓글이 없는 경우
        if (reviews.isNullOrEmpty()) {
            return emptyList()
        }
        return movieRepository
            .getMovies(reviews.map { it.movieId!!  })
            .mapNotNull { movie ->
                val relatedReview = reviews.find { it.movieId == movie.id }
                relatedReview?.let { ReviewedMovie(movie, it) }
            }
    }
}