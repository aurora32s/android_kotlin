package com.haman.aop_part5_chapter05.presentation.stationArrivals

import android.view.MenuItem
import com.haman.aop_part5_chapter05.data.repository.StationRepository
import com.haman.aop_part5_chapter05.domain.Station
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.lang.Exception

class StationArrivalsPresenter(
    private val view: StationArrivalsContract.View,
    private val station: Station,
    private val stationRepository: StationRepository
) : StationArrivalsContract.Presenter {

    override val scope: CoroutineScope = MainScope()

    override fun onViewCreated() {
        fetchStationArrivals()
    }

    override fun onDestroyView() {}

    override fun fetchStationArrivals() {
        scope.launch {
            try {
                view.showLoadingIndicator()
                view.showStationArrivals(
                    stationRepository.getStationArrivals(stationName = station.name)
                )
            } catch (exception: Exception) {
                exception.printStackTrace()
                view.showErrorDescription(exception.message ?: "알 수 없는 문제가 발생하였습니다.")
            } finally {
                view.hideLoadingIndecator()
            }
        }
    }

    override fun toggleStationFavorite(item: MenuItem) {
        scope.launch {
            stationRepository.updateStation(station.copy(isFavorite = item.isChecked))
        }
    }
}