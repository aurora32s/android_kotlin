package com.haman.aop_part6_chapter01.screen.main.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayoutMediator
import com.haman.aop_part6_chapter01.R
import com.haman.aop_part6_chapter01.data.entity.impl.LocationLatLngEntity
import com.haman.aop_part6_chapter01.data.entity.impl.MapSearchInfoEntity
import com.haman.aop_part6_chapter01.databinding.FragmentHomeBinding
import com.haman.aop_part6_chapter01.screen.base.BaseFragment
import com.haman.aop_part6_chapter01.screen.main.home.restaurant.RestaurantCategory
import com.haman.aop_part6_chapter01.screen.main.home.restaurant.RestaurantListFragment
import com.haman.aop_part6_chapter01.screen.mylocation.MyLocationActivity
import com.haman.aop_part6_chapter01.widget.adapter.impl.RestaurantListFragmentPagerAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {
    override val viewModel: HomeViewModel by viewModel()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    // view pager
    private lateinit var viewPagerAdapter: RestaurantListFragmentPagerAdapter

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: MyLocationListener

    // activityForResult 대신에 사용
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val responsePermissions = permissions.entries.filter {
                (it.key == Manifest.permission.ACCESS_FINE_LOCATION && it.value)
                        || (it.key == Manifest.permission.ACCESS_COARSE_LOCATION && it.value)
            }.filter { it.value }

            // success permission
            if (responsePermissions.size == locationPermissions.size) {
                setMyLocationListener()
            } else {
                with(binding.locationTextView) {
                    setText(R.string.request_location_manager)
                    setOnClickListener {
                        getMyLocation()
                    }
                }
                Toast.makeText(
                    requireContext(),
                    R.string.toast_not_assigned_location_permission,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun initViews() = with(binding) {
        locationTextView.setOnClickListener {
            viewModel.getMapSearchInfo()?.let { mapInfo ->
                changeLocationLauncher.launch(
                    MyLocationActivity.newIntent(
                        requireContext(), mapInfo
                    )
                )
            }
        }
    }

    // 위치 변경 launcher
    private val changeLocationLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.getParcelableExtra<MapSearchInfoEntity>(HomeViewModel.MY_LOCATION_KEY)
                    ?.let { myLocationInfo ->
                        viewModel.loadReverseGeoInformation(myLocationInfo.locationLatLng)
                    }
            }
        }

    private fun initViewsPager(locationLatLngEntity: LocationLatLngEntity) = with(binding) {
        val restaurantCategories = RestaurantCategory.values()

        if (::viewPagerAdapter.isInitialized.not()) {
            val restaurantLists = restaurantCategories.map {
                RestaurantListFragment.newInstance(it, locationLatLngEntity)
            }
            viewPagerAdapter = RestaurantListFragmentPagerAdapter(
                this@HomeFragment,
                restaurantLists,
                locationLatLngEntity
            )
            viewPager.adapter = viewPagerAdapter

            // 1. page 가 변경될 때마다 fragment 를 새로 만들지 않고 재사용
            viewPager.offscreenPageLimit = restaurantCategories.size
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.setText(restaurantCategories[position].categoryNameId)
            }.attach()
        }

        // 다른 장소인 경우
        if (locationLatLngEntity != viewPagerAdapter.locationLatLngEntity) {
            viewPagerAdapter.locationLatLngEntity = locationLatLngEntity
            viewPagerAdapter.fragmentList.forEach {
                it.viewModel.setLocationLatLng(locationLatLngEntity)
            }
        }
    }

    override fun observeData() = viewModel.homeStateLiveData.observe(viewLifecycleOwner) {
        when (it) {
            HomeState.UnInitialized -> getMyLocation()
            HomeState.Loading -> with(binding) {
                locationLoading.isVisible = true
                locationTextView.setText(R.string.loading)
            }
            is HomeState.Success -> {
                binding.locationLoading.isGone = true
                binding.locationTextView.text = it.mapSearchInfoEntity.fullAddress
                binding.tabLayout.isVisible = true
                binding.filterScrollView.isVisible = true
                binding.viewPager.isVisible = true
                initViewsPager(it.mapSearchInfoEntity.locationLatLng)

                // 내 위치와 최근 위치가 다른 경우
                if (it.isLocationSame.not()) {
                    Toast.makeText(requireContext(), R.string.request_check_location, Toast.LENGTH_SHORT).show()
                }
            }
            is HomeState.Error -> with(binding) {
                locationLoading.isGone = true
                locationTextView.setText(R.string.error_location_dis_found)
                Toast.makeText(requireContext(), it.messageId, Toast.LENGTH_SHORT).show()

                // 다시 현재 위치 요청
                locationTextView.setOnClickListener {
                    getMyLocation()
                }
            }
        }
    }

    // location permission check
    private fun getMyLocation() {
        // locationManager 가 초기화 되었는지 검사
        if (::locationManager.isInitialized.not()) {
            locationManager =
                requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        // GPS 가 켜져 있는지 확인
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (isGpsEnabled) {
            // 권한 확인
            locationPermissionLauncher.launch(locationPermissions)
        }
    }

    // request location permission
    @SuppressLint("MissingPermission")
    private fun setMyLocationListener() {
        val minTime = 1500L
        val minDistance = 100f
        if (::locationManager.isInitialized && ::locationListener.isInitialized.not()) {
            locationListener = MyLocationListener()
        }

        with(locationManager) {
            requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime,
                minDistance,
                locationListener
            )
            requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime,
                minDistance,
                locationListener
            )
        }
    }

    companion object {
        const val TAG = "HomeFragment"
        val locationPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        fun newInstance() = HomeFragment()
    }

    private fun removeLocationListener() {
        if (::locationManager.isInitialized && ::locationListener.isInitialized) {
            locationManager.removeUpdates(locationListener)
        }
    }

    inner class MyLocationListener : LocationListener {
        // 위치 변경 시 호출
        override fun onLocationChanged(location: Location) {
//            binding.locationTextView.text = "${location.latitude} / ${location.longitude}"
            // TODO viewModel 에서 poi 정보 요청
            viewModel.loadReverseGeoInformation(
                LocationLatLngEntity(
                    location.latitude,
                    location.longitude
                )
            )
            removeLocationListener()
        }
    }
}