package com.haman.aop_part5_chapter07.presentation.reviews

import com.haman.aop_part5_chapter07.domain.model.Movie
import com.haman.aop_part5_chapter07.domain.model.MovieReviews
import com.haman.aop_part5_chapter07.domain.model.Review
import com.haman.aop_part5_chapter07.domain.usecase.DeleteReviewUseCase
import com.haman.aop_part5_chapter07.domain.usecase.GetAllReviewsUseCase
import com.haman.aop_part5_chapter07.domain.usecase.SubmitReviewUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.lang.Exception

class MovieReviewsPresenter(
    private val view: MovieReviewsContract.View,
    override val movie: Movie,
    private val getAllReviews: GetAllReviewsUseCase,
    private val submitReview: SubmitReviewUseCase,
    private val deleteReview: DeleteReviewUseCase
) : MovieReviewsContract.Presenter {

    override val scope: CoroutineScope = MainScope()
    private var movieReviews: MovieReviews = MovieReviews(null, emptyList())

    override fun onViewCreated() {
        view.showMovieInformation(movie)
        fetchReviews()
    }

    override fun onDestroyView() {}

    private fun fetchReviews() = scope.launch {
        try {
            view.showLoadingIndicator()
            movieReviews = getAllReviews(movie.id!!)
            view.showReviews(movieReviews)
        } catch (exception: Exception) {
            exception.toString()
            view.showErrorDescription("에러가 발생했어요. 😢")
        } finally {
            view.hideLoadingIndicator()
        }
    }

    override fun requestAddReview(content: String, score: Float) {
        scope.launch {
            try {
                view.showLoadingIndicator()
                val submittedReview = submitReview(movie, content, score)
                view.showReviews(movieReviews.copy(myReview = submittedReview))
            } catch (exception: Exception) {
                exception.printStackTrace()
                view.showErrorToast("리뷰 등록을 실패했어요. 😢")
            } finally {
                view.hideLoadingIndicator()
            }
        }
    }

    override fun requestRemoveReview(review: Review) {
        scope.launch {
            try {
                view.showLoadingIndicator()
                deleteReview(review)
                view.showReviews(movieReviews.copy(myReview = null))
            } catch (exception: Exception) {
                exception.printStackTrace()
                view.showErrorToast("리뷰 삭제를 실패했어요 😢")
            } finally {
                view.hideLoadingIndicator()
            }
        }
    }
}