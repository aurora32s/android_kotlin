package com.haman.aop_part6_chapter01.screen.main.home.restaurant.detail.review

import android.content.Context
import androidx.core.os.bundleOf
import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantFoodEntity
import com.haman.aop_part6_chapter01.databinding.FragmentListBinding
import com.haman.aop_part6_chapter01.screen.base.BaseFragment
import com.haman.aop_part6_chapter01.screen.main.home.restaurant.detail.menu.RestaurantMenuListFragment
import org.koin.android.viewmodel.ext.android.viewModel

class RestaurantReviewListFragment :
    BaseFragment<RestaurantReviewListViewModel, FragmentListBinding>() {

    override val viewModel: RestaurantReviewListViewModel by viewModel()
    override fun getViewBinding(): FragmentListBinding =
        FragmentListBinding.inflate(layoutInflater)

    override fun observeData() {
    }

    companion object {
        const val RESTAURANT_ID_KEY = "restaurantId"

        fun newInstance(
            restaurantId: Long
        ): RestaurantReviewListFragment {
            val bundle = bundleOf(
                RESTAURANT_ID_KEY to restaurantId
            )
            return RestaurantReviewListFragment().apply {
                arguments = bundle
            }
        }
    }
}