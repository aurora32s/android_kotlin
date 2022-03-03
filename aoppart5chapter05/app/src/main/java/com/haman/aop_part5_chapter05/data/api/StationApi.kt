package com.haman.aop_part5_chapter05.data.api

import com.haman.aop_part5_chapter05.data.db.entity.StationEntity
import com.haman.aop_part5_chapter05.data.db.entity.SubwayEntity

interface StationApi {
    suspend fun getStationDataUpdatedTimeMills(): Long
    suspend fun getStationSubway(): List<Pair<StationEntity, SubwayEntity>>
}