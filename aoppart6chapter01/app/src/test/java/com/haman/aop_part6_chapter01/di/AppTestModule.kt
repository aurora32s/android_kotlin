package com.haman.aop_part6_chapter01.di

import com.google.firebase.auth.FirebaseAuth
import com.haman.aop_part6_chapter01.data.TestOrderRepository
import com.haman.aop_part6_chapter01.data.TestRestaurantFoodRepository
import com.haman.aop_part6_chapter01.data.TestRestaurantRepository
import com.haman.aop_part6_chapter01.data.entity.impl.LocationLatLngEntity
import com.haman.aop_part6_chapter01.data.repository.RestaurantRepository
import com.haman.aop_part6_chapter01.data.repository.food.RestaurantFoodRepository
import com.haman.aop_part6_chapter01.data.repository.order.OrderRepository
import com.haman.aop_part6_chapter01.screen.main.home.restaurant.RestaurantCategory
import com.haman.aop_part6_chapter01.screen.main.home.restaurant.RestaurantListViewModel
import com.haman.aop_part6_chapter01.screen.order.OrderMenuListViewModel
import com.haman.aop_part6_chapter01.viewmodel.order.OrderMenuListViewModelTest
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appTestModule = module {

    // viewmodel
    viewModel { (restaurantCategory: RestaurantCategory, locationLatLng: LocationLatLngEntity) ->
        RestaurantListViewModel(restaurantCategory, locationLatLng, get())
    }
    viewModel {OrderMenuListViewModel(get(), get())}

    // repository
    single<RestaurantRepository> { TestRestaurantRepository() }
    single<RestaurantFoodRepository> { TestRestaurantFoodRepository() }
    single<OrderRepository> { TestOrderRepository() }
}