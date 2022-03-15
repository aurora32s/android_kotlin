package com.haman.aop_part6_chapter01.screen.main.home.restaurant.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantEntity
import com.haman.aop_part6_chapter01.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantDetailViewModel(
    private val restaurantEntity: RestaurantEntity
): BaseViewModel() {

    private val _restaurantDetailStateLiveData = MutableLiveData<RestaurantDetailState>(RestaurantDetailState.UnInitialized)
    val restaurantDetailStateLiveData
        get() = _restaurantDetailStateLiveData

    override fun fetchData(): Job = viewModelScope.launch{
        _restaurantDetailStateLiveData.value = RestaurantDetailState.Success(
            restaurantEntity = restaurantEntity
        )
    }

    fun getRestaurantPhoneNumber(): String? {
        return when (val data = _restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                data.restaurantEntity.restaurantTelNumber
            }
            else -> null
        }
    }
}