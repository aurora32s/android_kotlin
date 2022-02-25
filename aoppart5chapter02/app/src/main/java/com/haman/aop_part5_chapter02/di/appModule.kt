package com.haman.aop_part5_chapter02.di

import com.haman.aop_part5_chapter02.data.db.provideDB
import com.haman.aop_part5_chapter02.data.db.provideToDoDao
import com.haman.aop_part5_chapter02.data.network.buildOkHttpClient
import com.haman.aop_part5_chapter02.data.network.provideGsonConverterFactory
import com.haman.aop_part5_chapter02.data.network.provideProductApiService
import com.haman.aop_part5_chapter02.data.network.provideProductRetrofit
import com.haman.aop_part5_chapter02.data.preference.PreferenceManager
import com.haman.aop_part5_chapter02.data.repository.DefaultProductRepository
import com.haman.aop_part5_chapter02.data.repository.ProductRepository
import com.haman.aop_part5_chapter02.domain.*
import com.haman.aop_part5_chapter02.domain.DeleteOrderedProductListUseCase
import com.haman.aop_part5_chapter02.domain.GetProductItemUseCase
import com.haman.aop_part5_chapter02.presentation.detail.ProductDetailViewModel
import com.haman.aop_part5_chapter02.presentation.list.ProductListViewModel
import com.haman.aop_part5_chapter02.presentation.main.MainViewModel
import com.haman.aop_part5_chapter02.presentation.profile.ProfileViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Coroutine Dispatchers
    single { Dispatchers.Main }
    single { Dispatchers.IO }

    // Repository
    single<ProductRepository> { DefaultProductRepository(get(), get(), get()) }

    // Retrofit
    single { buildOkHttpClient() }
    single { provideGsonConverterFactory() }
    single { provideProductRetrofit(get(), get()) }
    single { provideProductApiService(get()) }

    // UseCase
    factory { GetProductItemUseCase(get()) }
    factory { GetProductListUseCase(get()) }
    factory { OrderProductItemUseCase(get()) }
    factory { GetOrderedProductListUseCase(get()) }
    factory { DeleteOrderedProductListUseCase(get()) }

    // viewModel
    viewModel { MainViewModel() }
    viewModel { ProductListViewModel(get()) }
    viewModel { ProfileViewModel(get(), get(), get()) }
    viewModel { (productId: Long) ->
        ProductDetailViewModel(productId, get(), get())
    }

    // Room
    single { provideDB(androidApplication()) }
    single { provideToDoDao(get()) }

    // preference
    single { PreferenceManager(androidApplication()) }
}