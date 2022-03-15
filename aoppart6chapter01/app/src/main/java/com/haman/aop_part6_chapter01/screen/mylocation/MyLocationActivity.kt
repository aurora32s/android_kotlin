package com.haman.aop_part6_chapter01.screen.mylocation

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.haman.aop_part6_chapter01.R
import com.haman.aop_part6_chapter01.data.entity.impl.LocationLatLngEntity
import com.haman.aop_part6_chapter01.data.entity.impl.MapSearchInfoEntity
import com.haman.aop_part6_chapter01.databinding.ActivityMyLocationBinding
import com.haman.aop_part6_chapter01.screen.base.BaseActivity
import com.haman.aop_part6_chapter01.screen.main.home.HomeViewModel
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MyLocationActivity(

) : BaseActivity<MyLocationViewModel, ActivityMyLocationBinding>(), OnMapReadyCallback {


    override val viewModel: MyLocationViewModel by inject {
        parametersOf(
            intent.getParcelableExtra<MapSearchInfoEntity>(HomeViewModel.MY_LOCATION_KEY)
        )
    }

    override fun getViewBinding(): ActivityMyLocationBinding =
        ActivityMyLocationBinding.inflate(layoutInflater)

    private lateinit var map: GoogleMap

    private var isMapInitialized: Boolean = false
    private var isChangeLocation: Boolean = false
    override fun onMapReady(map: GoogleMap) {
        this.map = map
        viewModel.fetchData()
    }

    override fun observeData() = viewModel.myLocationStateLiveData.observe(this) {
        when (it) {
            MyLocationState.UnInitialized -> Unit
            MyLocationState.Loading -> {
                handleLoadingState()
            }
            is MyLocationState.Success -> {
                Log.d(".MyLocationActivity", it.toString())
                if (::map.isInitialized) {
                    handleSuccessState(it)
                }
            }
            is MyLocationState.Confirm -> {

            }
            is MyLocationState.Error -> {
                Toast.makeText(this, it.messageId, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun initViews() = with(binding) {
        super.initViews()

        // google map setting
        toolbar.setNavigationOnClickListener {
            finish()
        }
        confirmButton.setOnClickListener {

        }
        setupGoogleMap()
    }

    private fun setupGoogleMap() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun handleLoadingState() = with(binding) {
        locationLoading.isVisible = true
        locationTitleText.setText(R.string.loading)
    }

    private fun handleSuccessState(state: MyLocationState.Success) = with(binding) {
        val mapSearchInfo = state.mapSearchInfoEntity
        locationLoading.isGone = true
        locationTitleText.text = mapSearchInfo.fullAddress

        if (isMapInitialized.not()) {
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        mapSearchInfo.locationLatLng.latitude,
                        mapSearchInfo.locationLatLng.longitude
                    ),
                    CAMERA_ZOOM_LEVEL
                )
            )
            // 맴의 위치 이동이 종료되었을 때
            map.setOnCameraIdleListener {
                // 1초 이상 아무 동작이 없는 경우에만 위치 변경
                if (isChangeLocation.not()) {
                    isChangeLocation = true
                    Handler(Looper.getMainLooper()).postDelayed({
                        val cameraLatLng = map.cameraPosition.target
                        viewModel.changeLocationInfo(
                            LocationLatLngEntity(
                                cameraLatLng.latitude,
                                cameraLatLng.longitude
                            )
                        )
                        isChangeLocation = false
                    }, 1000)
                }
            }
            isMapInitialized = true
        }
    }

    companion object {
        fun newIntent(context: Context, mapSearchInfoEntity: MapSearchInfoEntity) =
            Intent(context, MyLocationActivity::class.java).apply {
                putExtra(HomeViewModel.MY_LOCATION_KEY, mapSearchInfoEntity)
            }

        const val CAMERA_ZOOM_LEVEL = 17f
    }
}