package com.haman.aop_part6_chapter01.screen.main.home.restaurant.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantEntity
import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantFoodEntity
import com.haman.aop_part6_chapter01.data.repository.food.RestaurantFoodRepository
import com.haman.aop_part6_chapter01.data.repository.user.UserRepository
import com.haman.aop_part6_chapter01.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantDetailViewModel(
    private val restaurantEntity: RestaurantEntity,
    private val userRepository: UserRepository,
    private val restaurantFoodRepository: RestaurantFoodRepository
): BaseViewModel() {

    private val _restaurantDetailStateLiveData = MutableLiveData<RestaurantDetailState>(RestaurantDetailState.UnInitialized)
    val restaurantDetailStateLiveData
        get() = _restaurantDetailStateLiveData

    override fun fetchData(): Job = viewModelScope.launch{
        _restaurantDetailStateLiveData.value = RestaurantDetailState.Loading
        val foods = restaurantFoodRepository.getFoods(
            restaurantId = restaurantEntity.restaurantInfoId,
            restaurantTitle = restaurantEntity.restaurantTitle
        )
        val foodMenuListInBasket = restaurantFoodRepository.getAllFoodMenuListInBasket()
        val isLiked = userRepository.getUserLikedRestaurant(restaurantEntity.restaurantTitle)
        _restaurantDetailStateLiveData.value = RestaurantDetailState.Success(
            restaurantEntity = restaurantEntity,
            restaurantFoodList = foods,
            foodMenuListInBasket = foodMenuListInBasket,
            isLiked = isLiked != null
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

    fun getRestaurantInfo(): RestaurantEntity? {
        return when (val data = _restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                data.restaurantEntity
            }
            else -> null
        }
    }

    fun toggleLikedRestaurant() = viewModelScope.launch{
        when (val data = _restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                userRepository.getUserLikedRestaurant(restaurantEntity.restaurantTitle)?.let {
                    // ?????? ????????? ??? ??????
                    userRepository.deleteUserLikedRestaurant(it.restaurantTitle)
                    _restaurantDetailStateLiveData.value = data.copy(
                        isLiked = false
                    )
                } ?: run {
                    userRepository.insertUserLikedRestaurant(restaurantEntity)
                    _restaurantDetailStateLiveData.value = data.copy(
                        isLiked = true
                    )
                }
            }
        }
    }

    // ????????? ????????? ??????????????? ??????
    fun notifyFoodMenuListInBasket(foodEntity: RestaurantFoodEntity) = viewModelScope.launch {
        when(val data = _restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                _restaurantDetailStateLiveData.value = data.copy(
                    foodMenuListInBasket = data.foodMenuListInBasket?.toMutableList()?.apply {
                        add(foodEntity)
                    }
                )
            }
            else -> Unit
        }
    }

    // ?????? ?????? ?????? ?????? ??????
    fun notifyClearMenuAlertInBasket(isClearNeed: Boolean, cb: () -> Unit) {
        when(val data = _restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                _restaurantDetailStateLiveData.value = data.copy(
                    isClearNeedInBasketAndAction = Pair(isClearNeed, cb)
                )
            }
            else -> Unit
        }
    }

    // ???????????? ?????????
    fun notifyClearBasket() = viewModelScope.launch {
        when(val data = _restaurantDetailStateLiveData.value) {
            is RestaurantDetailState.Success -> {
                _restaurantDetailStateLiveData.value = data.copy(
                    foodMenuListInBasket = listOf(),
                    isClearNeedInBasketAndAction = Pair(false, {})
                )
            }
            else -> Unit
        }
    }
}