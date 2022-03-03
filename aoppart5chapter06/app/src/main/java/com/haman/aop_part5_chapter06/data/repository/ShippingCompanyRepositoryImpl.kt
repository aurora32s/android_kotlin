package com.haman.aop_part5_chapter06.data.repository

import com.haman.aop_part5_chapter06.data.api.SweetTrackerApi
import com.haman.aop_part5_chapter06.data.db.ShippingCompanyDao
import com.haman.aop_part5_chapter06.data.entity.ShippingCompany
import com.haman.aop_part5_chapter06.data.preference.PreferenceManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ShippingCompanyRepositoryImpl(
    private val trackerApi: SweetTrackerApi, // api
    private val shippingCompanyDao: ShippingCompanyDao, // database
    private val preferenceManager: PreferenceManager, // shared preference
    private val dispatcher: CoroutineDispatcher
): ShippingCompanyRepository {

    override suspend fun getShippingCompanies(): List<ShippingCompany> = withContext(dispatcher){
        val currentTimeMills = System.currentTimeMillis()
        val lastDatabaseUpdatedTimeMills = preferenceManager.getLong(
            KEY_LAST_DATABASE_UPDATED_TIME_MILLIS
        )
        // 처음 요청하거나, 요청해야 하는 기간이 지난 경우
        if (lastDatabaseUpdatedTimeMills == null ||
                CACHE_MAX_AGE_MILLIS < (currentTimeMills - lastDatabaseUpdatedTimeMills)) {
            // 택배 회사 리스트
            val shippingCompanies = trackerApi.getShippingCompanies()
                .body()
                ?.shippingCompanies
                ?: emptyList()
            shippingCompanyDao.insert(shippingCompanies)
            preferenceManager.putLong(KEY_LAST_DATABASE_UPDATED_TIME_MILLIS, currentTimeMills)
        }

        shippingCompanyDao.getAll()
    }

    companion object {
        private const val KEY_LAST_DATABASE_UPDATED_TIME_MILLIS = "KEY_LAST_DATABASE_UPDATED_TIME_MILLIS"
        private const val CACHE_MAX_AGE_MILLIS = 1000L * 60 * 60 * 24 * 7
    }
}