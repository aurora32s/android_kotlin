package com.haman.aop_part6_chapter01.screen.main.home.restaurant.detail.review

import android.content.Context
import android.icu.text.CaseMap
import androidx.core.os.bundleOf
import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantFoodEntity
import com.haman.aop_part6_chapter01.databinding.FragmentListBinding
import com.haman.aop_part6_chapter01.model.review.ReviewModel
import com.haman.aop_part6_chapter01.screen.base.BaseFragment
import com.haman.aop_part6_chapter01.screen.main.home.restaurant.detail.menu.RestaurantMenuListFragment
import com.haman.aop_part6_chapter01.util.provider.ResourcesProvider
import com.haman.aop_part6_chapter01.widget.adapter.ModelRecyclerAdapter
import com.haman.aop_part6_chapter01.widget.adapter.listener.AdapterListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.experimental.property.inject

class RestaurantReviewListFragment :
    BaseFragment<RestaurantReviewListViewModel, FragmentListBinding>() {

    override val viewModel: RestaurantReviewListViewModel by viewModel {
        parametersOf(
            arguments?.getString(RESTAURANT_TITLE_KEY)
        )
    }
    private val resourcesProvider by inject<ResourcesProvider>()

    override fun getViewBinding(): FragmentListBinding =
        FragmentListBinding.inflate(layoutInflater)

    private val adapter by lazy {
        ModelRecyclerAdapter<ReviewModel, RestaurantReviewListViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : AdapterListener {

            }
        )
    }

    override fun initViews() {
        super.initViews()
        binding.recyclerView.adapter = adapter
    }

    override fun observeData() = viewModel.reviewStateLiveDate.observe(viewLifecycleOwner) {
        when (it) {
            RestaurantReviewState.UnInitialized -> {

            }
            RestaurantReviewState.Loading -> {

            }
            is RestaurantReviewState.Success -> {
                handleSuccess(it)
            }
        }
    }

    private fun handleSuccess(state: RestaurantReviewState.Success) {
        adapter.submitList(state.reviewList)
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