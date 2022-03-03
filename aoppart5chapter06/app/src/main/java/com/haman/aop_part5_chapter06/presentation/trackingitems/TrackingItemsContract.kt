package com.haman.aop_part5_chapter06.presentation.trackingitems

import com.haman.aop_part5_chapter06.data.entity.TrackingInformation
import com.haman.aop_part5_chapter06.data.entity.TrackingItem
import com.haman.aop_part5_chapter06.presentation.BasePresenter
import com.haman.aop_part5_chapter06.presentation.BaseView

class TrackingItemsContract {

    interface View: BaseView<Presenter> {
        fun showLoadingIndicator()
        fun hideLoadingIndicator()
        fun showNoDataDescription()
        fun showTrackingItemInformation(
            trackingItemInformation: List<Pair<TrackingItem, TrackingInformation>>
        )
    }

    interface Presenter: BasePresenter {
        var trackingItemInformation: List<Pair<TrackingItem, TrackingInformation>>
        fun refresh()
    }

}