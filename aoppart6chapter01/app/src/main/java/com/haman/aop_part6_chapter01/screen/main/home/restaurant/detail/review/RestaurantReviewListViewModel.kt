package com.haman.aop_part6_chapter01.screen.main.home.restaurant.detail.review

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haman.aop_part6_chapter01.data.entity.impl.ReviewEntity
import com.haman.aop_part6_chapter01.data.repository.review.DefaultRestaurantReviewRepository
import com.haman.aop_part6_chapter01.data.repository.review.RestaurantReviewRepository
import com.haman.aop_part6_chapter01.model.review.ReviewModel
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
        when(val reviews = restaurantReviewRepository.getReviews(restaurantTitle)) {
            is DefaultRestaurantReviewRepository.Result.Error -> {
                _reviewStateLiveData.value = RestaurantReviewState.Error(
                    reviews.e
                )
            }
            is DefaultRestaurantReviewRepository.Result.Success<*> -> {
                _reviewStateLiveData.value = RestaurantReviewState.Success(
                    (reviews.data as List<ReviewEntity>).map {
                        ReviewModel(
                            id = it.id,
                            title = it.title,
                            description = it.content,
                            grade = it.rating.toInt(),
                            thumbnailImageUri = if (it.imagesUrlList?.isNotEmpty() ?: false) {
                                Uri.parse(it.imagesUrlList?.first())
                            } else {
                                null
                            }
                        )
                    }
                )
            }
        }
    }

}