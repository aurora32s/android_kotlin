package com.haman.aop_part5_chapter06.data.repository

import com.haman.aop_part5_chapter06.data.entity.TrackingInformation
import com.haman.aop_part5_chapter06.data.entity.TrackingItem
import kotlinx.coroutines.flow.Flow

interface TrackingItemRepository {

    val trackingItems: Flow<List<TrackingItem>>
    suspend fun getTrackingItemInformations(): List<Pair<TrackingItem, TrackingInformation>>
    suspend fun getTrackingInformation(code: String, invoice: String): TrackingInformation?
    suspend fun saveTrackingItem(trackingItem: TrackingItem)
    suspend fun deleteTrackingItem(trackingItem: TrackingItem)

}