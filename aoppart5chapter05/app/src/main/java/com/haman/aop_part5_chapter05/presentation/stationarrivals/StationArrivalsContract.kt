package com.haman.aop_part5_chapter05.presentation.stationArrivals

import android.view.MenuItem
import com.haman.aop_part5_chapter05.domain.ArrivalInformation
import com.haman.aop_part5_chapter05.domain.Station
import com.haman.aop_part5_chapter05.presentation.BasePresenter
import com.haman.aop_part5_chapter05.presentation.BaseView

interface StationArrivalsContract {

    interface View: BaseView<Presenter> {
        fun showLoadingIndicator()
        fun hideLoadingIndecator()
        fun showErrorDescription(message: String)
        fun showStationArrivals(arrivalInformation: List<ArrivalInformation>)
    }

    interface Presenter: BasePresenter {
        fun fetchStationArrivals()
        fun toggleStationFavorite(item: MenuItem)
    }
}