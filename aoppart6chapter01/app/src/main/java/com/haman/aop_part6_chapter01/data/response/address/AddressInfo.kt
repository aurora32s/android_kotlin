package com.haman.aop_part6_chapter01.data.response.address

import com.haman.aop_part6_chapter01.data.entity.impl.LocationLatLngEntity
import com.haman.aop_part6_chapter01.data.entity.impl.MapSearchInfoEntity

data class AddressInfo(
    val fullAddress: String?,
    val addressKey: String,
    val roadAddressKey: String,
    val addressType: String,
    val cityDo: String,
    val guGun: String,
    val eupMyun: String,
    val adminDong: String,
    val adminDongCode: String,
    val legalDong: String,
    val legalDongCode: String,
    val ri: String,
    val roadName: String,
    val buildingIndex: String,
    val buildingName: String?,
    val mappingDistance: String,
    val roadCode: String
) {
    fun toSearchInfoEntity(locationLatLngEntity: LocationLatLngEntity) = MapSearchInfoEntity(
        fullAddress = fullAddress ?: "위치 정보 없음",
        name = buildingName ?: "빌딩 정보 없음",
        locationLatLng = locationLatLngEntity
    )
}