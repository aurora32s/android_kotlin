package com.haman.aop_part6_chapter01.data.repository.map

import com.haman.aop_part6_chapter01.data.entity.impl.LocationLatLngEntity
import com.haman.aop_part6_chapter01.data.response.address.AddressInfo

interface MapRepository {

    // reverse geo coding
    suspend fun getReverseGeoInformation(
        locationLatLngEntity: LocationLatLngEntity
    ): AddressInfo?

}