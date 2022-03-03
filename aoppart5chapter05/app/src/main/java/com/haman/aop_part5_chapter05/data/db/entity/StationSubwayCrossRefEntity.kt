package com.haman.aop_part5_chapter05.data.db.entity

import androidx.room.Entity

/**
 * N-N 관계
 */
@Entity(primaryKeys = ["stationName","subwayId"])
data class StationSubwayCrossRefEntity(
    val stationName: String,
    val subwayId: Int
)
