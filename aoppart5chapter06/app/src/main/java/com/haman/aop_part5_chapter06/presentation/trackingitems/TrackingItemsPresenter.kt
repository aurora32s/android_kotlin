package com.haman.aop_part5_chapter06.presentation.trackingitems

import com.haman.aop_part5_chapter06.data.entity.TrackingInformation
import com.haman.aop_part5_chapter06.data.entity.TrackingItem
import com.haman.aop_part5_chapter06.data.repository.TrackingItemRepository
import com.haman.aop_part5_chapter06.presentation.BasePresenter
import kotlinx.coroutines.launch
import java.lang.Exception

class TrackingItemsPresenter(
    private val view: TrackingItemsFragment,
    private val trackingItemRepository: TrackingItemRepository
) : TrackingItemsContract.Presenter {
    override var trackingItemInformation: List<Pair<TrackingItem, TrackingInformation>> =
        emptyList()

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
                trackingItemInformation = trackingItemRepository.getTrackingItemInformation()
            }
            if (trackingItemInformation.isEmpty()) {
                view.showNoDataDescription()
            } else {
                view.showTrackingItemInformation(trackingItemInformation)
            }
        } catch (exception: Exception) {

        } finally {
            view.hideLoadingIndicator()
        }
    }
}