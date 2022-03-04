package com.haman.aop_part5_chapter06.presentation.trackinghistory

import com.haman.aop_part5_chapter06.data.entity.TrackingInformation
import com.haman.aop_part5_chapter06.data.entity.TrackingItem
import com.haman.aop_part5_chapter06.data.repository.TrackingItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.lang.Exception

class TrackingHistoryPresenter(
    private val view: TrackingHistoryContract.View,
    private val trackerRepository: TrackingItemRepository,
    private val trackingItem: TrackingItem,
    private var trackingInformation: TrackingInformation
): TrackingHistoryContract.Presenter {

    override val scope: CoroutineScope = MainScope()

    override fun onViewCreated() {
        view.showTrackingItemInformation(trackingItem, trackingInformation)
    }

    override fun onDestroyView() {}

    override fun refresh() {
        scope.launch {
            try {
                view.showLoadingIndicator()
                val newTrackingInformation =
                    trackerRepository.getTrackingInformation(
                        trackingItem.company.code,
                        trackingItem.invoice
                    )
                newTrackingInformation?.let {
                    trackingInformation = it
                    view.showTrackingItemInformation(trackingItem, trackingInformation)
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            } finally {
                view.hideLoadingIndicator()
            }
        }
    }

    /**
     * 저장된 송장번호 삭제
     */
    override fun deleteTrackingItem() {
        scope.launch {
            try {
                trackerRepository.deleteTrackingItem(trackingItem)
                view.finish()
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }
}