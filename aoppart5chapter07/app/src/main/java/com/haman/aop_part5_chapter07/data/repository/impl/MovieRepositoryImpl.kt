package com.haman.aop_part5_chapter07.data.repository.impl

import com.haman.aop_part5_chapter07.data.api.MovieApi
import com.haman.aop_part5_chapter07.data.repository.MovieRepository
import com.haman.aop_part5_chapter07.domain.model.Movie
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * 영화 관리 repository implements
 */
class MovieRepositoryImpl(
    private val movieApi: MovieApi,
    private val dispatcher: CoroutineDispatcher
) : MovieRepository {

    override suspend fun getAllMovies(): List<Movie> = withContext(dispatcher) {
        movieApi.getAllMovies()
    }

    override suspend fun getMovies(movieId: List<String>): List<Movie> = withContext(dispatcher){
        movieApi.getMovies(movieId)
    }
}