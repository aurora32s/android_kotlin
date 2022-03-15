package com.haman.aop_part6_chapter01.di

import com.haman.aop_part6_chapter01.data.network.MapApiService
import com.haman.aop_part6_chapter01.data.repository.restaurant.DefaultRestaurantRepository
import com.haman.aop_part6_chapter01.data.repository.RestaurantRepository
import com.haman.aop_part6_chapter01.data.repository.map.DefaultMapRepository
import com.haman.aop_part6_chapter01.data.repository.map.MapRepository
import com.haman.aop_part6_chapter01.screen.main.home.HomeViewModel
import com.haman.aop_part6_chapter01.screen.main.home.restaurant.RestaurantCategory
import com.haman.aop_part6_chapter01.screen.main.home.restaurant.RestaurantListViewModel
import com.haman.aop_part6_chapter01.screen.main.mypage.MyPageViewModel
import com.haman.aop_part6_chapter01.util.provider.ResourcesProvider
import com.haman.aop_part6_chapter01.util.provider.impl.DefaultResourceProvider
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {
    single<ResourcesProvider> { DefaultResourceProvider(androidContext()) }

    // dispatcher
    single { Dispatchers.IO }
    single { Dispatchers.Main }

    // repository
    single<RestaurantRepository> { DefaultRestaurantRepository(get(), get()) }
    single<MapRepository> { DefaultMapRepository(get(),get()) }

    // api
    single { provideGsonConvertFactory() }
    single { buildOkHttpClient() }
    single { provideMapRetrofit(get(),get())}
    single { provideMapApiService(get()) }

    // viewModel
    viewModel { HomeViewModel(get()) }
    viewModel { MyPageViewModel() }
    viewModel { (restaurantCategory: RestaurantCategory) -> RestaurantListViewModel(restaurantCategory, get()) }
}
