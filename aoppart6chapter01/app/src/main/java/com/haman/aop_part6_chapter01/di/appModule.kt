package com.haman.aop_part6_chapter01.di

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.haman.aop_part6_chapter01.data.entity.impl.LocationLatLngEntity
import com.haman.aop_part6_chapter01.data.entity.impl.MapSearchInfoEntity
import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantEntity
import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantFoodEntity
import com.haman.aop_part6_chapter01.data.network.MapApiService
import com.haman.aop_part6_chapter01.data.preference.AppPreferenceManager
import com.haman.aop_part6_chapter01.data.repository.restaurant.DefaultRestaurantRepository
import com.haman.aop_part6_chapter01.data.repository.RestaurantRepository
import com.haman.aop_part6_chapter01.data.repository.food.DefaultRestaurantFoodRepository
import com.haman.aop_part6_chapter01.data.repository.food.RestaurantFoodRepository
import com.haman.aop_part6_chapter01.data.repository.map.DefaultMapRepository
import com.haman.aop_part6_chapter01.data.repository.map.MapRepository
import com.haman.aop_part6_chapter01.data.repository.order.DefaultOrderRepository
import com.haman.aop_part6_chapter01.data.repository.order.OrderRepository
import com.haman.aop_part6_chapter01.data.repository.review.DefaultRestaurantReviewRepository
import com.haman.aop_part6_chapter01.data.repository.review.RestaurantReviewRepository
import com.haman.aop_part6_chapter01.data.repository.user.DefaultUserRepository
import com.haman.aop_part6_chapter01.data.repository.user.UserRepository
import com.haman.aop_part6_chapter01.screen.main.home.HomeViewModel
import com.haman.aop_part6_chapter01.screen.main.home.restaurant.RestaurantCategory
import com.haman.aop_part6_chapter01.screen.main.home.restaurant.RestaurantListViewModel
import com.haman.aop_part6_chapter01.screen.main.home.restaurant.detail.RestaurantDetailViewModel
import com.haman.aop_part6_chapter01.screen.main.home.restaurant.detail.menu.RestaurantMenuListViewModel
import com.haman.aop_part6_chapter01.screen.main.home.restaurant.detail.review.RestaurantReviewListViewModel
import com.haman.aop_part6_chapter01.screen.main.like.RestaurantLikedListViewModel
import com.haman.aop_part6_chapter01.screen.main.mypage.MyPageViewModel
import com.haman.aop_part6_chapter01.screen.mylocation.MyLocationViewModel
import com.haman.aop_part6_chapter01.screen.order.OrderMenuListViewModel
import com.haman.aop_part6_chapter01.util.event.MenuChangeEventBus
import com.haman.aop_part6_chapter01.util.provider.ResourcesProvider
import com.haman.aop_part6_chapter01.util.provider.impl.DefaultResourceProvider
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {
    single<ResourcesProvider> { DefaultResourceProvider(androidContext()) }

    // FireStore
    single { Firebase.firestore }

    // dispatcher
    single { Dispatchers.IO }
    single { Dispatchers.Main }

    // repository
    single<RestaurantRepository> { DefaultRestaurantRepository(get(), get(), get()) }
    single<MapRepository> { DefaultMapRepository(get(),get()) }
    single<UserRepository> { DefaultUserRepository(get(), get(),get()) }
    single<RestaurantFoodRepository> { DefaultRestaurantFoodRepository(get(), get(), get()) }
    single<RestaurantReviewRepository> { DefaultRestaurantReviewRepository(get()) }
    single<OrderRepository> { DefaultOrderRepository(get(), get()) }

    // preference
    single { AppPreferenceManager(androidContext()) }

    // api
    single { provideGsonConvertFactory() }
    single { buildOkHttpClient() }
    single(named("map")) { provideMapRetrofit(get(),get())}
    single(named("food")) { provideFoodRetrofit(get(),get()) }
    single { provideMapApiService(get(qualifier = named("map"))) }
    single { provideFoodApiService(get(qualifier = named("food"))) }

    // dao
    single { provideDB(androidContext()) }
    single { provideLocationDao(get()) }
    single { provideRestaurantDao(get()) }
    single { provideFoodMenuBasketDao(get()) }

    // viewModel
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { MyPageViewModel(get(), get(), get()) }
    viewModel { (restaurantCategory: RestaurantCategory, locationLatLng: LocationLatLngEntity) ->
        RestaurantListViewModel(restaurantCategory, locationLatLng, get())
    }
    viewModel { (mapSearchInfoEntity: MapSearchInfoEntity) -> MyLocationViewModel(mapSearchInfoEntity, get(), get())}
    viewModel { (restaurant: RestaurantEntity) -> RestaurantDetailViewModel(restaurant, get(), get())}
    viewModel { (restaurantId: Long, foodList: List<RestaurantFoodEntity>) ->
        RestaurantMenuListViewModel(restaurantId, foodList, get())
    }
    viewModel { (restaurantTitle: String) -> RestaurantReviewListViewModel(restaurantTitle, get()) }
    viewModel { RestaurantLikedListViewModel(get()) }
    viewModel { OrderMenuListViewModel(get(), get()) }

    // event bus
    single { MenuChangeEventBus() }
}
