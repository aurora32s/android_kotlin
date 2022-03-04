package com.haman.aop_part5_chapter06.presentation.trackinghistory

import com.haman.aop_part5_chapter06.data.entity.TrackingInformation
import com.haman.aop_part5_chapter06.data.entity.TrackingItem
import com.haman.aop_part5_chapter06.presentation.BasePresenter
import com.haman.aop_part5_chapter06.presentation.BaseView

class TrackingHistoryContract {

    interface View : BaseView<Presenter> {
        fun showLoadingIndicator() // 택배 정보 요청 로딩 show
        fun hideLoadingIndicator() // 택배 정보 요청 로딩 hide
        // 택배 정보 화면
        fun showTrackingItemInformation(trackingItem: TrackingItem, trackingInformation: TrackingInformation)
        // 삭제 시 종료
        fun finish()
    }

    interface Presenter : BasePresenter {
        fun refresh()
        fun deleteTrackingItem()
    }

}