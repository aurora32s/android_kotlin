package com.haman.aop_part6_chapter01.screen.main.like

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantEntity
import com.haman.aop_part6_chapter01.data.repository.user.UserRepository
import com.haman.aop_part6_chapter01.model.CellType
import com.haman.aop_part6_chapter01.model.restaurant.RestaurantModel
import com.haman.aop_part6_chapter01.model.restaurant.toModel
import com.haman.aop_part6_chapter01.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantLikedListViewModel(
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _restaurantListLiveData = MutableLiveData<List<RestaurantModel>>()
    val restaurantListLiveData
        get() = _restaurantListLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        _restaurantListLiveData.value = userRepository.getAllUserLikedRestaurant().map {
            it.toModel(CellType.LIKED_RESTAURANT_CELL)
        }
    }

    fun dislikeRestaurant(restaurant: RestaurantEntity) = viewModelScope.launch {
        userRepository.deleteUserLikedRestaurant(
            restaurantTitle = restaurant.restaurantTitle
        )
        fetchData()
    }
}