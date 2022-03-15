package com.haman.aop_part6_chapter01.screen.mylocation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haman.aop_part6_chapter01.R
import com.haman.aop_part6_chapter01.data.entity.impl.LocationLatLngEntity
import com.haman.aop_part6_chapter01.data.entity.impl.MapSearchInfoEntity
import com.haman.aop_part6_chapter01.data.repository.map.MapRepository
import com.haman.aop_part6_chapter01.screen.base.BaseViewModel
import com.haman.aop_part6_chapter01.screen.main.home.HomeState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyLocationViewModel(
    private val mapSearchInfoEntity: MapSearchInfoEntity,
    private val mapRepository: MapRepository,
    private val userRepository:
): BaseViewModel() {

    private val _myLocationStateLiveData = MutableLiveData<MyLocationState>(MyLocationState.UnInitialized)
    val myLocationStateLiveData
        get() = _myLocationStateLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        myLocationStateLiveData.value = MyLocationState.Loading
        myLocationStateLiveData.value = MyLocationState.Success(
            mapSearchInfoEntity
        )
    }

    fun changeLocationInfo(
        locationLatLngEntity: LocationLatLngEntity
    ) = viewModelScope.launch {
        myLocationStateLiveData.value = MyLocationState.Loading

        val addressInfo = mapRepository.getReverseGeoInformation(locationLatLngEntity)
        addressInfo?.let { info ->
            _myLocationStateLiveData.value = MyLocationState.Success(
                mapSearchInfoEntity = info.toSearchInfoEntity(locationLatLngEntity)
            )
        } ?: kotlin.run {
            _myLocationStateLiveData.value = MyLocationState.Error(
                R.string.fail_to_load_address_info
            )
        }
    }

    fun confirmSelectLocation() = viewModelScope.launch {
        when (val data = _myLocationStateLiveData.value) {
            is MyLocationState.Success -> {
                // 성공한 경우에만 처리

            }
        }
    }
}