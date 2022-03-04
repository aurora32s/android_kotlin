package com.haman.aop_part5_chapter07.domain.usecase

import com.haman.aop_part5_chapter07.data.repository.MovieRepository
import com.haman.aop_part5_chapter07.domain.model.Movie

/**
 * 모든 영화 정보 요청 Use Case
 */
class GetAllMoviesUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(): List<Movie> =
        movieRepository.getAllMovies()
}