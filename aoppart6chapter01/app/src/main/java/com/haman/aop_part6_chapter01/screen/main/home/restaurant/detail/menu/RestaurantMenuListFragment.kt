package com.haman.aop_part6_chapter01.screen.main.home.restaurant.detail.menu

import androidx.core.os.bundleOf
import com.haman.aop_part6_chapter01.data.entity.impl.RestaurantFoodEntity
import com.haman.aop_part6_chapter01.databinding.FragmentListBinding
import com.haman.aop_part6_chapter01.model.food.FoodModel
import com.haman.aop_part6_chapter01.screen.base.BaseFragment
import com.haman.aop_part6_chapter01.util.provider.ResourcesProvider
import com.haman.aop_part6_chapter01.widget.adapter.ModelRecyclerAdapter
import com.haman.aop_part6_chapter01.widget.adapter.listener.AdapterListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RestaurantMenuListFragment :
    BaseFragment<RestaurantMenuListViewModel, FragmentListBinding>() {

    private val resourceProvider by inject<ResourcesProvider>()

    private val restaurantId by lazy { arguments?.getLong(RESTAURANT_ID_KEY, -1) }
    private val foodList by lazy {
        arguments?.getParcelableArrayList<RestaurantFoodEntity>(
            FOOD_LIST_KEY
        )
    }
    override val viewModel: RestaurantMenuListViewModel by viewModel {
        parametersOf(
            restaurantId,
            foodList
        )
    }

    override fun getViewBinding(): FragmentListBinding =
        FragmentListBinding.inflate(layoutInflater)

    private val adapter by lazy {
        ModelRecyclerAdapter<FoodModel, RestaurantMenuListViewModel>(
            listOf(),
            viewModel,
            resourceProvider,
            adapterListener = object : AdapterListener {

            }
        )
    }

    override fun initViews() = with(binding) {
        super.initViews()
        recyclerView.adapter = adapter
    }

    override fun observeData() = viewModel.foodListLiveData.observe(this) {
        adapter.submitList(it)
    }

    companion object {

        const val RESTAURANT_ID_KEY = "restaurantId"
        const val FOOD_LIST_KEY = "foodList"

        fun newInstance(
            restaurantId: Long,
            foodList: ArrayList<RestaurantFoodEntity>
        ): RestaurantMenuListFragment {
            val bundle = bundleOf(
                RESTAURANT_ID_KEY to restaurantId,
                FOOD_LIST_KEY to foodList
            )
            return RestaurantMenuListFragment().apply {
                arguments = bundle
            }
        }
    }
}