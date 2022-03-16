package com.haman.aop_part6_chapter01.screen.main.home.restaurant.detail.review

import android.content.Context
import android.icu.text.CaseMap
import androidx.core.os.bundleOf
import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantFoodEntity
import com.haman.aop_part6_chapter01.databinding.FragmentListBinding
import com.haman.aop_part6_chapter01.screen.base.BaseFragment
import com.haman.aop_part6_chapter01.screen.main.home.restaurant.detail.menu.RestaurantMenuListFragment
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RestaurantReviewListFragment :
    BaseFragment<RestaurantReviewListViewModel, FragmentListBinding>() {

    override val viewModel: RestaurantReviewListViewModel by viewModel {
        parametersOf(
            arguments?.getString(RESTAURANT_TITLE_KEY)
        )
    }
    override fun getViewBinding(): FragmentListBinding =
        FragmentListBinding.inflate(layoutInflater)

    override fun observeData() = viewModel.reviewStateLiveDate.observe(viewLifecycleOwner) {
        when (it) {
            RestaurantReviewState.UnInitialized -> {

            }
            RestaurantReviewState.Loading -> {

            }
            is RestaurantReviewState.Success -> {

            }
        }
    }

    private fun handleSuccess(state: RestaurantReviewState.Success) {

    }

    companion object {
        const val RESTAURANT_TITLE_KEY = "restaurantTitle"

        fun newInstance(
            restaurantTitle: String
        ): RestaurantReviewListFragment {
            val bundle = bundleOf(
                RESTAURANT_TITLE_KEY to restaurantTitle
            )
            return RestaurantReviewListFragment().apply {
                arguments = bundle
            }
        }
    }
}