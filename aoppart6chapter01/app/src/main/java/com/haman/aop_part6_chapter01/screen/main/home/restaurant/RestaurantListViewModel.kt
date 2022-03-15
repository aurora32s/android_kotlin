package com.haman.aop_part6_chapter01.screen.main.home.restaurant

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haman.aop_part6_chapter01.data.entity.impl.LocationLatLngEntity
import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantEntity
import com.haman.aop_part6_chapter01.data.repository.RestaurantRepository
import com.haman.aop_part6_chapter01.model.CellType
import com.haman.aop_part6_chapter01.model.restaurant.RestaurantModel
import com.haman.aop_part6_chapter01.model.restaurant.toModel
import com.haman.aop_part6_chapter01.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantListViewModel(
    private val restaurantCategory: RestaurantCategory,
    private var locationLatLngEntity: LocationLatLngEntity,
    private val restaurantRepository: RestaurantRepository,
    private var restaurantOrder: RestaurantOrder = RestaurantOrder.DEFAULT
) : BaseViewModel() {

    private val _restaurantListLiveData = MutableLiveData<List<RestaurantModel>>()
    val restaurantListLiveData
        get() = _restaurantListLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        val restaurantList = restaurantRepository.getList(restaurantCategory, locationLatLngEntity)
        val sortedList = getSortedList(restaurantList)
        _restaurantListLiveData.value = sortedList.map { it.toModel(CellType.RESTAURANT_CELL) }
    }

    fun setLocationLatLng(locationLatLngEntity: LocationLatLngEntity) {
        this.locationLatLngEntity = locationLatLngEntity
        fetchData()
    }

    private fun getSortedList(restaurantList: List<RestaurantEntity>) = when(restaurantOrder) {
        RestaurantOrder.DEFAULT -> {restaurantList}
        RestaurantOrder.FAST_DELIVERY -> {
            restaurantList.sortedBy { it.deliveryTimeRange.first }
        }
        RestaurantOrder.LOW_DELIVERY_TIP -> {
            restaurantList.sortedBy { it.deliveryTipRange.first }
        }
        RestaurantOrder.TOP_RATE -> {
            restaurantList.sortedBy { it.grade }
        }
    }

    fun setRestaurantOrder(order: RestaurantOrder) {
        this.restaurantOrder = order
        fetchData()
    }
}