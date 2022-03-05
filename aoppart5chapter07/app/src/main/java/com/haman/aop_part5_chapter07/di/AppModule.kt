package com.haman.aop_part5_chapter07.di

import android.app.Activity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.haman.aop_part5_chapter07.data.api.MovieApi
import com.haman.aop_part5_chapter07.data.api.ReviewApi
import com.haman.aop_part5_chapter07.data.api.UserApi
import com.haman.aop_part5_chapter07.data.api.impl.MovieFireStoreApi
import com.haman.aop_part5_chapter07.data.api.impl.ReviewFireStoreApi
import com.haman.aop_part5_chapter07.data.api.impl.UserFireStoreApi
import com.haman.aop_part5_chapter07.data.preference.PreferenceManager
import com.haman.aop_part5_chapter07.data.preference.SharedPreferenceManager
import com.haman.aop_part5_chapter07.data.repository.MovieRepository
import com.haman.aop_part5_chapter07.data.repository.ReviewRepository
import com.haman.aop_part5_chapter07.data.repository.UserRepository
import com.haman.aop_part5_chapter07.data.repository.impl.MovieRepositoryImpl
import com.haman.aop_part5_chapter07.data.repository.impl.ReviewRepositoryImpl
import com.haman.aop_part5_chapter07.data.repository.impl.UserRepositoryImpl
import com.haman.aop_part5_chapter07.domain.model.Movie
import com.haman.aop_part5_chapter07.domain.usecase.GetAllMoviesUseCase
import com.haman.aop_part5_chapter07.domain.usecase.GetAllReviewsUseCase
import com.haman.aop_part5_chapter07.domain.usecase.GetRandomFeaturedMovieUseCase
import com.haman.aop_part5_chapter07.presentation.home.HomeContract
import com.haman.aop_part5_chapter07.presentation.home.HomeFragment
import com.haman.aop_part5_chapter07.presentation.home.HomePresenter
import com.haman.aop_part5_chapter07.presentation.reviews.MovieReviewsContract
import com.haman.aop_part5_chapter07.presentation.reviews.MovieReviewsFragment
import com.haman.aop_part5_chapter07.presentation.reviews.MovieReviewsPresenter
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { Dispatchers.IO }
}

val dataModule = module {
    single { Firebase.firestore }

    // api
    single<MovieApi> { MovieFireStoreApi(get()) }
    single<ReviewApi> { ReviewFireStoreApi(get()) }
    single<UserApi> { UserFireStoreApi(get()) }

    // preference
    single { androidContext().getSharedPreferences("preference", Activity.MODE_PRIVATE) }
    single<PreferenceManager> { SharedPreferenceManager(get()) }

    // repository
    single<MovieRepository> { MovieRepositoryImpl(get(),get()) }
    single<ReviewRepository> { ReviewRepositoryImpl(get(),get()) }
    single<UserRepository> { UserRepositoryImpl(get(),get(),get()) }
}

val domainModule = module {
    factory { GetRandomFeaturedMovieUseCase(get(), get()) }
    factory { GetAllMoviesUseCase(get()) }
    factory { GetAllReviewsUseCase(get(), get()) }
}

val presenterModule = module {
    scope<HomeFragment> {
        scoped<HomeContract.Presenter> { HomePresenter(getSource(),get(),get()) }
    }
    scope<MovieReviewsFragment> {
        scoped<MovieReviewsContract.Presenter> { (movie: Movie) ->
            MovieReviewsPresenter(getSource(), movie, get())
        }
    }
}