package com.haman.aop_part5_chapter06.data.repository

import com.haman.aop_part5_chapter06.data.api.SweetTrackerApi
import com.haman.aop_part5_chapter06.data.db.TrackingItemDao
import com.haman.aop_part5_chapter06.data.entity.TrackingInformation
import com.haman.aop_part5_chapter06.data.entity.TrackingItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class TrackingItemRepositoryImpl(
    private val trackerApi: SweetTrackerApi, // api
    private val trackingItemDao: TrackingItemDao, // database
    private val dispatcher: CoroutineDispatcher
): TrackingItemRepository {

    // 택배사 코드와 송장 번호로 정보 받아오기
    override suspend fun getTrackingItemInformation(): List<Pair<TrackingItem, TrackingInformation>> =
        withContext(dispatcher) {
            trackingItemDao.getAll() // DB에 저장되어 있는 송장 번호와 택배사 코드 가져오기
                .mapNotNull { trackingItem -> // 결과가 null 이 아닌 경우
                    val relatedTrackingInfo = trackerApi.getTrackingInformation(
                        trackingItem.company.code, // 택배사 코드
                        trackingItem.invoice // 송장 번호
                    ).body()

                    // 송장 번호가 없는 경우
                    if (relatedTrackingInfo?.invoiceNo.isNullOrBlank()) {
                        null
                    } else {
                        // tracking item 정보별 information 전달
                        trackingItem to relatedTrackingInfo!!
                    }
                }
                .sortedWith(
                    compareBy(
                        { it.second.level }, // 오름차순
                        { -(it.second.lastDetail?.time ?: Long.MAX_VALUE) } // 내림차순
                    )
                )
        }

}