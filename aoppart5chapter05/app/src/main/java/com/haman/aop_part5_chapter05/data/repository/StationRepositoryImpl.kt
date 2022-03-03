package com.haman.aop_part5_chapter05.data.repository

import com.haman.aop_part5_chapter05.data.api.StationApi
import com.haman.aop_part5_chapter05.data.api.StationArrivalsApi
import com.haman.aop_part5_chapter05.data.api.response.mapper.toArrivalInformation
import com.haman.aop_part5_chapter05.data.db.StationDao
import com.haman.aop_part5_chapter05.data.db.entity.mapper.toStationEntity
import com.haman.aop_part5_chapter05.data.db.entity.mapper.toStations
import com.haman.aop_part5_chapter05.domain.Station
import com.haman.aop_part5_chapter05.data.preference.PreferenceManager
import com.haman.aop_part5_chapter05.domain.ArrivalInformation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.lang.RuntimeException

class StationRepositoryImpl(
    private val stationArrivalsApi: StationArrivalsApi,
    private val stationApi: StationApi,
    private val stationDao: StationDao,
    private val preferenceManager: PreferenceManager,
    private val dispatcher: CoroutineDispatcher
) : StationRepository {

    override val stations: Flow<List<Station>> =
        stationDao.getStationWithSubways()
            .distinctUntilChanged() // 과도한 호출을 방지
            .map { stations ->
                stations.toStations().sortedByDescending { it.isFavorite }
            }
            .flowOn(dispatcher) // thread setting

    override suspend fun refreshStations() = withContext(dispatcher) {
        val fileUpdatedTimeMills = stationApi.getStationDataUpdatedTimeMills()
        val lastDatabaseUpdatedTimeMills = preferenceManager.getLong(
            KEY_LAST_DATABASE_UPDATED_TIME_MILLS
        )
        // 지하철 업로드 필요
        if (lastDatabaseUpdatedTimeMills == null || lastDatabaseUpdatedTimeMills < fileUpdatedTimeMills) {
            stationDao.insertStationSubways(stationApi.getStationSubway()) // List<Pair<>>
            preferenceManager.putLong(KEY_LAST_DATABASE_UPDATED_TIME_MILLS, fileUpdatedTimeMills)
        }
    }

    override suspend fun getStationArrivals(stationName: String): List<ArrivalInformation> =
        withContext(dispatcher) {
            stationArrivalsApi.getRealtimeStationArrival(stationName)
                .body()
                ?.realtimeArrivalList
                ?.toArrivalInformation()
                ?.distinctBy { it.direction } // 중복된 정보는 가장 최신 정보를 가져온다.
                ?.sortedBy { it.subway }
                ?: throw RuntimeException("도착 정보를 불러오는 도중 에러가 발생하였습니다.")
        }

    override suspend fun updateStation(station: Station) {
        stationDao.updateStation(station.toStationEntity())
    }

    companion object {
        private const val KEY_LAST_DATABASE_UPDATED_TIME_MILLS = "KEY_LAST_DATABASE_UPDATED_TIME_MILLS"
    }
}