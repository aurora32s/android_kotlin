package com.haman.aop_part6_chapter01.screen.main.like

import android.util.Log
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.haman.aop_part6_chapter01.data.db.dao.LocationDao
import com.haman.aop_part6_chapter01.databinding.FragmentRestaurantLikedBinding
import com.haman.aop_part6_chapter01.model.restaurant.RestaurantModel
import com.haman.aop_part6_chapter01.model.restaurant.toEntity
import com.haman.aop_part6_chapter01.screen.base.BaseFragment
import com.haman.aop_part6_chapter01.screen.main.home.restaurant.detail.RestaurantDetailActivity
import com.haman.aop_part6_chapter01.util.provider.ResourcesProvider
import com.haman.aop_part6_chapter01.widget.adapter.ModelRecyclerAdapter
import com.haman.aop_part6_chapter01.widget.adapter.listener.AdapterListener
import com.haman.aop_part6_chapter01.widget.adapter.listener.like.RestaurantLikedListListener
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class RestaurantLikedListFragment :
    BaseFragment<RestaurantLikedListViewModel, FragmentRestaurantLikedBinding>() {

    override val viewModel: RestaurantLikedListViewModel by viewModel()
    private val resourcesProvider by inject<ResourcesProvider>()

    override fun getViewBinding(): FragmentRestaurantLikedBinding =
        FragmentRestaurantLikedBinding.inflate(layoutInflater)

    private val adapter by lazy {
        ModelRecyclerAdapter<RestaurantModel, RestaurantLikedListViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : RestaurantLikedListListener {
                override fun onDislikeItem(model: RestaurantModel) {
                    viewModel.dislikeRestaurant(model.toEntity())
                }

                override fun onClickItem(model: RestaurantModel) {
                    startActivity(
                        RestaurantDetailActivity.newIntent(requireContext(), model.toEntity())
                    )
                }
            }
        )
    }

    override fun initViews() {
        super.initViews()
        binding.recyclerView.adapter = adapter
    }

    override fun observeData() = viewModel.restaurantListLiveData.observe((viewLifecycleOwner) ){
        Log.d("Restaurant", it.toString())
        checkListEmpty(it)
    }

    private fun checkListEmpty(restaurantList: List<RestaurantModel>) {
        val isEmpty = restaurantList.isEmpty()
        binding.recyclerView.isGone = isEmpty
        binding.emptyResultTextView.isVisible = isEmpty

        if (isEmpty.not()) {
            adapter.submitList(restaurantList)
        }
    }

    companion object {
        const val TAG = "RestaurantLikedListFragment"

        fun newInstance() = RestaurantLikedListFragment()
    }
}