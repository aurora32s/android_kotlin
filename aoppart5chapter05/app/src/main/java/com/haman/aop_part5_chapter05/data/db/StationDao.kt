package com.haman.aop_part5_chapter05.data.db

import androidx.room.*
import com.haman.aop_part5_chapter05.data.db.entity.StationEntity
import com.haman.aop_part5_chapter05.data.db.entity.StationSubwayCrossRefEntity
import com.haman.aop_part5_chapter05.data.db.entity.StationWithSubwaysEntity
import com.haman.aop_part5_chapter05.data.db.entity.SubwayEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StationDao {

    /**
     * flow: Observable read (Jetpack: LiveData<T>)
     * suspend: One-shot write / One-shot read
     */
    @Transaction // insert, delete 등은 내부적으로 transaction 지원
    @Query("SELECT * FROM StationEntity")
    fun getStationWithSubways(): Flow<List<StationWithSubwaysEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(station: List<StationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubways(subways: List<SubwayEntity>)

    /**
     * station 과 subway 간의 관계도 함께 저장
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossReferences(reference: List<StationSubwayCrossRefEntity>)

    /**
     * 연속된 작업을 하나로 관리하기 위해서 별도로 구현
     * distinctUntilChanged 가 transaction 단위로 호출
     */
    @Transaction
    suspend fun insertStationSubways(stationSubways: List<Pair<StationEntity,SubwayEntity>>) {
        insertStations(stationSubways.map { it.first })
        insertSubways(stationSubways.map { it.second })
        insertCrossReferences(
            stationSubways.map { (station, subway) ->
                StationSubwayCrossRefEntity(
                    stationName = station.stationName,
                    subwayId = subway.subwayId
                )
            }
        )
    }

    @Update
    suspend fun updateStation(station: StationEntity)
}