package com.haman.aop_part6_chapter01.data.repository.user

import com.haman.aop_part6_chapter01.data.db.dao.LocationDao
import com.haman.aop_part6_chapter01.data.entity.impl.LocationLatLngEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultUserRepository(
    private val locationDao: LocationDao,
    private val ioDispatcher: CoroutineDispatcher
): UserRepository {

    override suspend fun getUserLocation(): LocationLatLngEntity? =
        withContext(ioDispatcher) {
            locationDao.get(-1)
        }

    override suspend fun insertUserLocation(locationLatLngEntity: LocationLatLngEntity) =
        withContext(ioDispatcher) {
            locationDao.insert(locationLatLngEntity)
        }

}