package com.haman.aop_part5_chapter07.presentation.reviews

import com.haman.aop_part5_chapter07.domain.model.Movie
import com.haman.aop_part5_chapter07.domain.model.MovieReviews
import com.haman.aop_part5_chapter07.domain.model.Review
import com.haman.aop_part5_chapter07.presentation.BasePresenter
import com.haman.aop_part5_chapter07.presentation.BaseView

interface MovieReviewsContract {

    interface View : BaseView<Presenter> {
        fun showLoadingIndicator()
        fun hideLoadingIndicator()
        fun showErrorDescription(message: String)
        fun showMovieInformation(movie: Movie)
        fun showReviews(reviews: MovieReviews)
        fun showErrorToast(message: String)
    }

    interface Presenter : BasePresenter {
        val movie: Movie // 현재 선택된 영화정보

        fun requestAddReview(content: String, score: Float) // 댓글 작성
        fun requestRemoveReview(review: Review) // 댓글 삭제
    }

}