package com.haman.aop_part5_chapter06.data.repository

import com.haman.aop_part5_chapter06.data.entity.TrackingInformation
import com.haman.aop_part5_chapter06.data.entity.TrackingItem
import kotlinx.coroutines.flow.Flow

interface TrackingItemRepository {

    val trackingItems: Flow<List<TrackingItem>>
    suspend fun getTrackingItemInformation(): List<Pair<TrackingItem, TrackingInformation>>
    suspend fun saveTrackingItem(trackingItem: TrackingItem)

}