package com.haman.aop_part5_chapter07.di

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.haman.aop_part5_chapter07.data.api.MovieApi
import com.haman.aop_part5_chapter07.data.api.ReviewApi
import com.haman.aop_part5_chapter07.data.api.impl.MovieFireStoreApi
import com.haman.aop_part5_chapter07.data.api.impl.ReviewFireStoreApi
import com.haman.aop_part5_chapter07.data.repository.MovieRepository
import com.haman.aop_part5_chapter07.data.repository.ReviewRepository
import com.haman.aop_part5_chapter07.data.repository.impl.MovieRepositoryImpl
import com.haman.aop_part5_chapter07.data.repository.impl.ReviewRepositoryImpl
import com.haman.aop_part5_chapter07.domain.usecase.GetAllMoviesUseCase
import com.haman.aop_part5_chapter07.domain.usecase.GetRandomFeaturedMovieUseCase
import com.haman.aop_part5_chapter07.presentation.home.HomeContract
import com.haman.aop_part5_chapter07.presentation.home.HomeFragment
import com.haman.aop_part5_chapter07.presentation.home.HomePresenter
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val appModule = module {
    single { Dispatchers.IO }
}

val dataModule = module {
    single { Firebase.firestore }

    // api
    single<MovieApi> { MovieFireStoreApi(get()) }
    single<ReviewApi> { ReviewFireStoreApi(get()) }

    // repository
    single<MovieRepository> { MovieRepositoryImpl(get(),get()) }
    single<ReviewRepository> { ReviewRepositoryImpl(get(),get()) }
}

val domainModule = module {
    factory { GetRandomFeaturedMovieUseCase(get(), get()) }
    factory { GetAllMoviesUseCase(get()) }
}

val presenterModule = module {
    scope<HomeFragment> {
        scoped<HomeContract.Presenter> { HomePresenter(getSource(),get(),get()) }
    }
}