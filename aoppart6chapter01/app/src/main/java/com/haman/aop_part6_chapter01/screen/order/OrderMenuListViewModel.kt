package com.haman.aop_part6_chapter01.screen.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.haman.aop_part6_chapter01.R
import com.haman.aop_part6_chapter01.data.repository.food.RestaurantFoodRepository
import com.haman.aop_part6_chapter01.data.repository.order.DefaultOrderRepository
import com.haman.aop_part6_chapter01.data.repository.order.OrderRepository
import com.haman.aop_part6_chapter01.model.CellType
import com.haman.aop_part6_chapter01.model.food.FoodModel
import com.haman.aop_part6_chapter01.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OrderMenuListViewModel(
    private val restaurantFoodRepository: RestaurantFoodRepository,
    private val orderRepository: OrderRepository
) : BaseViewModel() {

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val _orderStateLiveData = MutableLiveData<OrderMenuState>(OrderMenuState.UnInitialized)
    val orderStateLiveData
        get() = _orderStateLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        _orderStateLiveData.value = OrderMenuState.Loading
        val foodMenuList = restaurantFoodRepository.getAllFoodMenuListInBasket()
        _orderStateLiveData.value = OrderMenuState.Success(
            foodMenuList.map {
                FoodModel(
                    id = it.hashCode().toLong(),
                    type = CellType.ORDER_FOOD_CELL,
                    title = it.title,
                    description = it.description,
                    price = it.price,
                    imageUrl = it.imageUrl,
                    restaurantId = it.restaurantId,
                    foodId = it.id,
                    restaurantTitle = it.restaurantTitle
                )
            }
        )
    }

    fun orderMenu() = viewModelScope.launch {
        val foodMenuList = restaurantFoodRepository.getAllFoodMenuListInBasket()
        val restaurantTitle = foodMenuList.first().restaurantTitle
        if (foodMenuList.isNotEmpty()) {
            val restaurantId = foodMenuList.first().restaurantId
            firebaseAuth.currentUser?.let { user ->
                val result = orderRepository.orderMenu(
                    user.uid,
                    restaurantId,
                    foodMenuList,
                    restaurantTitle
                )
                when (result) {
                    is DefaultOrderRepository.Result.Error -> {
                        _orderStateLiveData.value =
                            OrderMenuState.Error(R.string.error_to_order, result.e)
                    }
                    is DefaultOrderRepository.Result.Success<*> -> {
                        restaurantFoodRepository.clearFoodMenuListInBasket()
                        _orderStateLiveData.value = OrderMenuState.Order
                    }
                }
            } ?: kotlin.run {
                _orderStateLiveData.value = OrderMenuState.Error(R.string.error_to_user, Exception())
            }
        }
    }

    fun clearOrderMenu() = viewModelScope.launch {
        restaurantFoodRepository.clearFoodMenuListInBasket()
        fetchData()
    }

    fun removeOrderMenu(foodModel: FoodModel) = viewModelScope.launch {
        restaurantFoodRepository.removeFoodMenuListInBasket(foodModel.foodId)
        fetchData()
    }
}