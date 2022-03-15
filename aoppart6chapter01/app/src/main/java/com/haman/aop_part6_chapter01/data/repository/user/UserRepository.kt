package com.haman.aop_part6_chapter01.data.repository.user

import com.haman.aop_part6_chapter01.data.entity.impl.LocationLatLngEntity

interface UserRepository {

    suspend fun getUserLocation(): LocationLatLngEntity?
    suspend fun insertUserLocation(locationLatLngEntity: LocationLatLngEntity)

}