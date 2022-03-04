package com.haman.aop_part5_chapter06.presentation.trackingitems

import com.haman.aop_part5_chapter06.data.entity.TrackingInformation
import com.haman.aop_part5_chapter06.data.entity.TrackingItem
import com.haman.aop_part5_chapter06.data.repository.TrackingItemRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class TrackingItemsPresenter(
    private val view: TrackingItemsFragment,
    private val trackingItemRepository: TrackingItemRepository
) : TrackingItemsContract.Presenter {

    override var trackingItemInformation: List<Pair<TrackingItem, TrackingInformation>> =
        emptyList()

    init {
        scope.launch {
            // trackingItems 가 변경되었을 때 호출
            trackingItemRepository
                .trackingItems
                .collect { refresh() } // live data 에 observe
        }
    }

    override fun onViewCreated() {
        fetchTrackingInformation()
    }

    override fun onDestroyView() {}

    override fun refresh() {
        fetchTrackingInformation(true)
    }

    private fun fetchTrackingInformation(forceFetch: Boolean = false) = scope.launch {
        try {
            view.showLoadingIndicator()

            if (trackingItemInformation.isEmpty() || forceFetch) {
                trackingItemInformation = trackingItemRepository.getTrackingItemInformations()
            }
            if (trackingItemInformation.isEmpty()) {
                view.showNoDataDescription()
            } else {
                view.showTrackingItemInformation(trackingItemInformation)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        } finally {
            view.hideLoadingIndicator()
        }
    }
}