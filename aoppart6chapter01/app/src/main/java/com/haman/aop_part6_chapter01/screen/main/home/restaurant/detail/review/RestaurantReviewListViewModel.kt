package com.haman.aop_part6_chapter01.screen.main.home.restaurant.detail.review

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haman.aop_part6_chapter01.data.repository.review.RestaurantReviewRepository
import com.haman.aop_part6_chapter01.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RestaurantReviewListViewModel(
    private val restaurantTitle: String,
    private val restaurantReviewRepository: RestaurantReviewRepository
): BaseViewModel() {

    private val _reviewStateLiveData = MutableLiveData<RestaurantReviewState>(RestaurantReviewState.UnInitialized)
    val reviewStateLiveDate
        get() = _reviewStateLiveData

    override fun fetchData(): Job = viewModelScope.launch{
        _reviewStateLiveData.value = RestaurantReviewState.Loading
        val reviews = restaurantReviewRepository.getReviews(restaurantTitle)
        _reviewStateLiveData.value = RestaurantReviewState.Success(
            reviews
        )
    }

}