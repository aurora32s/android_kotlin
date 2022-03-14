package com.haman.aop_part6_chapter01.screen.main.home

import com.google.android.material.tabs.TabLayoutMediator
import com.haman.aop_part6_chapter01.databinding.FragmentHomeBinding
import com.haman.aop_part6_chapter01.screen.base.BaseFragment
import com.haman.aop_part6_chapter01.screen.main.home.restaurant.RestaurantCategory
import com.haman.aop_part6_chapter01.screen.main.home.restaurant.RestaurantListFragment
import com.haman.aop_part6_chapter01.widget.adapter.impl.RestaurantListFragmentPagerAdapter
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class HomeFragment: BaseFragment<HomeViewModel, FragmentHomeBinding>() {
    override val viewModel: HomeViewModel by viewModel()

    override fun getViewBinding(): FragmentHomeBinding
        = FragmentHomeBinding.inflate(layoutInflater)

    // view pager
    private lateinit var viewPagerAdapter: RestaurantListFragmentPagerAdapter

    override fun observeData() {
    }

    override fun initViews() {
        super.initViews()
        initViewsPager()
    }

    private fun initViewsPager() = with(binding) {
        val restaurantCategories = RestaurantCategory.values()

        if (::viewPagerAdapter.isInitialized.not()) {
            val restaurantLists = restaurantCategories.map {
                RestaurantListFragment.newInstance(it)
            }
            viewPagerAdapter = RestaurantListFragmentPagerAdapter(
                this@HomeFragment,
                restaurantLists
            )
            viewPager.adapter = viewPagerAdapter
        }
        // 1. page 가 변경될 때마다 fragment 를 새로 만들지 않고 재사용
        viewPager.offscreenPageLimit = restaurantCategories.size
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setText(restaurantCategories[position].categoryNameId)
        }.attach()
    }

    companion object {
        const val TAG = "HomeFragment"
        fun newInstance() = HomeFragment()
    }
}