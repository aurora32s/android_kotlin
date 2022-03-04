package com.haman.aop_part5_chapter06.work

import androidx.work.DelegatingWorkerFactory
import com.haman.aop_part5_chapter06.data.repository.TrackingItemRepository
import kotlinx.coroutines.CoroutineDispatcher

/**
 * default worker factory 가 아닌,
 * 위임 받은 factory 를 사용
 */
class AppWorkerFactory(
    trackingItemRepository: TrackingItemRepository,
    dispatcher: CoroutineDispatcher
): DelegatingWorkerFactory() {

    init {
        // 사용할 factory 추가
        addFactory(TrackingCheckWorkerFactory(trackingItemRepository, dispatcher))
    }
}