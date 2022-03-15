package com.haman.aop_part6_chapter01.screen.main.home.restaurant.detail

import android.content.Context
import android.content.Intent
import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantEntity
import com.haman.aop_part6_chapter01.databinding.ActivityRestaurantDetailBinding
import com.haman.aop_part6_chapter01.screen.base.BaseActivity
import com.haman.aop_part6_chapter01.screen.main.home.restaurant.RestaurantListFragment
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RestaurantDetailActivity :
    BaseActivity<RestaurantDetailViewModel, ActivityRestaurantDetailBinding>() {

    override val viewModel by viewModel<RestaurantDetailViewModel> {
        parametersOf(
            intent.getParcelableExtra<RestaurantEntity>(RestaurantListFragment.RESTAURANT_KEY)
        )
    }

    override fun getViewBinding(): ActivityRestaurantDetailBinding =
        ActivityRestaurantDetailBinding.inflate(layoutInflater)

    override fun observeData() = viewModel.restaurantDetailStateLiveData.observe(this) {
        when(it) {
            RestaurantDetailState.Loading -> TODO()
            is RestaurantDetailState.Success -> TODO()
            RestaurantDetailState.UnInitialized -> TODO()
        }
    }

    companion object {
        fun newIntent(context: Context, restaurantEntity: RestaurantEntity) =
            Intent(context, RestaurantDetailActivity::class.java).apply {
                putExtra(RestaurantListFragment.RESTAURANT_KEY, restaurantEntity)
            }
    }
}