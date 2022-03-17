package com.haman.aop_part6_chapter01.screen.main.mypage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.haman.aop_part6_chapter01.R
import com.haman.aop_part6_chapter01.data.entity.impl.OrderEntity
import com.haman.aop_part6_chapter01.data.preference.AppPreferenceManager
import com.haman.aop_part6_chapter01.data.repository.order.DefaultOrderRepository
import com.haman.aop_part6_chapter01.data.repository.order.OrderRepository
import com.haman.aop_part6_chapter01.data.repository.user.UserRepository
import com.haman.aop_part6_chapter01.model.order.OrderModel
import com.haman.aop_part6_chapter01.screen.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MyPageViewModel(
    private val preferenceManager: AppPreferenceManager,
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository
) : BaseViewModel() {

    private val _myStateLiveData = MutableLiveData<MyPageState>(MyPageState.UnInitialized)
    val myStateLiveData
        get() = _myStateLiveData

    fun saveToken(token: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            preferenceManager.putIdToken(token)
            fetchData()
        }
    }

    fun setUserInfo(firebaseUser: FirebaseUser?) = viewModelScope.launch {
        firebaseUser?.let { user ->
            when (val orderMenuResult = orderRepository.getAllOrderMenus(user.uid)) {
                is DefaultOrderRepository.Result.Error -> {
                    _myStateLiveData.value = MyPageState.Error(
                        R.string.error_to_order_history,
                        orderMenuResult.e
                    )
                }
                is DefaultOrderRepository.Result.Success<*> -> {
                    val orderList = orderMenuResult.data as List<OrderEntity>
                    _myStateLiveData.value = MyPageState.Success.Registered(
                        userName = user.displayName ?: "익명",
                        profileImageUri = user.photoUrl,
                        orderList = orderList.map {
                            OrderModel(
                                id = it.hashCode().toLong(),
                                orderId = it.id,
                                userId = it.userId,
                                restaurantId = it.restaurantId,
                                foodMenuList = it.foodMenuList,
                                restaurantTitle = it.restaurantTitle
                            )
                        }
                    )
                }
            }
        } ?: kotlin.run {
            _myStateLiveData.value = MyPageState.Success.NotRegistered
        }
    }

    override fun fetchData(): Job = viewModelScope.launch {
        try {
            _myStateLiveData.value = MyPageState.Loading
            preferenceManager.getIdToken()?.let {
                _myStateLiveData.value = MyPageState.Login(it)
            } ?: kotlin.run {
                _myStateLiveData.value = MyPageState.Success.NotRegistered
            }
        } catch (exception: Exception) {
            _myStateLiveData.value = MyPageState.Error(R.string.error_location_dis_found, exception)
        }
    }

    fun signOut() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            preferenceManager.removeIdToken()
        }
        userRepository.deleteAllUserLikedRestaurant()
        fetchData()
    }
}