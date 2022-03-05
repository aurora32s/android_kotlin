package com.haman.aop_part5_chapter07.presentation.home

import com.haman.aop_part5_chapter07.domain.model.FeaturedMovie
import com.haman.aop_part5_chapter07.domain.model.Movie
import com.haman.aop_part5_chapter07.presentation.BasePresenter
import com.haman.aop_part5_chapter07.presentation.BaseView

class HomeContract {

    interface View: BaseView<Presenter> {
        fun showLoadingIndicator()
        fun hideLoadingIndicator()
        fun showErrorDescription(message: String)
        fun showMovies(
            featuredMovie: FeaturedMovie?,
            movies: List<Movie>
        )
    }

    interface Presenter: BasePresenter

}