package com.haman.aop_part6_chapter01.viewmodel.restaurant

import com.haman.aop_part6_chapter01.data.entity.impl.LocationLatLngEntity
import com.haman.aop_part6_chapter01.data.repository.RestaurantRepository
import com.haman.aop_part6_chapter01.model.CellType
import com.haman.aop_part6_chapter01.model.restaurant.toModel
import com.haman.aop_part6_chapter01.screen.main.home.restaurant.RestaurantCategory
import com.haman.aop_part6_chapter01.screen.main.home.restaurant.RestaurantListViewModel
import com.haman.aop_part6_chapter01.screen.main.home.restaurant.RestaurantOrder
import com.haman.aop_part6_chapter01.viewmodel.ViewModelTest
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.koin.core.parameter.parametersOf
import org.koin.test.inject

internal class RestaurantListViewModelTest: ViewModelTest() {

    /**
     * [RestaurantCategory]
     * [LocationLatLngEntity]
     */
    private var restaurantCategory = RestaurantCategory.ALL
    private val locationLatLngEntity = LocationLatLngEntity(0.0, 0.0)
    private val restaurantRepository by inject<RestaurantRepository>()

    private val restaurantListViewModel by inject<RestaurantListViewModel>{
        parametersOf(
            restaurantCategory,
            locationLatLngEntity
        )
    }

    @Test
    fun `test load restaurant list category ALL`() = runBlockingTest {
        val testObservable = restaurantListViewModel.restaurantListLiveData.test()

        restaurantListViewModel.fetchData()
        testObservable.assertValueSequence(
            listOf(
                restaurantRepository.getList(restaurantCategory, locationLatLngEntity).map {
                    it.toModel(CellType.RESTAURANT_CELL)
                }
            )
        )
    }

    @Test
    fun `test load restaurant list category Excepted`() = runBlockingTest {
        restaurantCategory = RestaurantCategory.CAFE_DESSERT

        val testObservable = restaurantListViewModel.restaurantListLiveData.test()

        restaurantListViewModel.fetchData()
        testObservable.assertValueSequence(
            listOf(
                listOf()
            )
        )
    }

    @Test
    fun `test load restaurant list order by fast delivery time `() = runBlockingTest {
        val testObservable = restaurantListViewModel.restaurantListLiveData.test()

        restaurantListViewModel.setRestaurantOrder(RestaurantOrder.FAST_DELIVERY)

        testObservable.assertValueSequence(
            listOf(
                restaurantRepository.getList(restaurantCategory, locationLatLngEntity)
                    .sortedBy { it.deliveryTimeRange.first }
                    .map {
                        it.toModel(CellType.RESTAURANT_CELL)
                    }
            )
        )
    }
}