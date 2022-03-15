package com.haman.aop_part6_chapter01.screen.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haman.aop_part6_chapter01.R
import com.haman.aop_part6_chapter01.data.entity.impl.LocationLatLngEntity
import com.haman.aop_part6_chapter01.data.repository.map.MapRepository
import com.haman.aop_part6_chapter01.screen.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(
    private val mapRepository: MapRepository
): BaseViewModel() {

    private val _homeStateLiveData = MutableLiveData<HomeState>(HomeState.UnInitialized)
    val homeStateLiveData
        get() = _homeStateLiveData

    // gps 정보로 poi 데이터 요청
    fun loadReverseGeoInformation(
        locationLatLngEntity: LocationLatLngEntity
    ) = viewModelScope.launch {
        _homeStateLiveData.value = HomeState.Loading

        val addressInfo = mapRepository.getReverseGeoInformation(locationLatLngEntity)
        addressInfo?.let { info ->
            _homeStateLiveData.value = HomeState.Success(
                mapSearchInfoEntity = info.toSearchInfoEntity(locationLatLngEntity)
            )
        } ?: kotlin.run {
            _homeStateLiveData.value = HomeState.Error(
                R.string.fail_to_load_address_info
            )
        }
    }
}