package com.haman.aop_part6_chapter01.screen.main.home.restaurant.detail.menu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantFoodEntity
import com.haman.aop_part6_chapter01.model.food.FoodModel
import com.haman.aop_part6_chapter01.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantMenuListViewModel(
    private val restaurantId: Long,
    private val foodEntityList: List<RestaurantFoodEntity>
): BaseViewModel() {

    private val _foodListLiveData = MutableLiveData<List<FoodModel>>()
    val foodListLiveData
        get() = _foodListLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        _foodListLiveData.value = foodEntityList.map {
            FoodModel(
                id = it.hashCode().toLong(),
                title = it.title,
                description = it.description,
                price = it.price,
                imageUrl = it.imageUrl,
                restaurantId = it.restaurantId
            )
        }
    }

}